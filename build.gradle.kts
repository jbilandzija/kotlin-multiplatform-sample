@file:Suppress("UNUSED_VARIABLE")

import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

val kotlinVersion = "1.7.10"
val serializationVersion = "1.3.2"
val ktorVersion = "2.1.1"
val logbackVersion = "1.2.11"
val cowsayVersion = "1.1.0"
val assertJVersion = "3.22.0"

// SEE: https://github.com/JetBrains/kotlin-wrappers
val kotlinReactVersion = "18.2.0-pre.388"
val kotlinStyledNextVersion = "1.2.1-pre.388"
val kotlinCssVersion = "1.0.0-pre.388"
val kotlinEmotion = "11.10.4-pre.388" // Compatible to React 18

plugins {
    application
    kotlin("multiplatform") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.1.2" // Helper for single JAR generation
    id("io.github.turansky.kfc.legacy-union") version "5.8.0" // FIXES ISSUE: https://youtrack.jetbrains.com/issue/KT-51921
    id("io.gitlab.arturbosch.detekt") version "1.20.0"
}

group = "com.bilandzija"
version = "0.0.1-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

kotlin {
    jvm {
        withJava()
    }
    js {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
            binaries.executable()
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
                implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
                implementation("io.ktor:ktor-server-host-common-jvm:$ktorVersion")
                implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-server-compression:$ktorVersion")
                implementation("io.ktor:ktor-server-call-logging-jvm:$ktorVersion")

                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

                implementation("ch.qos.logback:logback-classic:$logbackVersion")

                // ERROR HANDLING
                implementation("com.github.ricksbrown:cowsay:$cowsayVersion")
                implementation("com.github.ricksbrown:cowjar-extra:$cowsayVersion")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation("io.ktor:ktor-server-test-host:$ktorVersion")
                implementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
                implementation("org.assertj:assertj-core:$assertJVersion")
            }
        }

        val jsMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")

                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:$kotlinReactVersion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:$kotlinReactVersion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-css:$kotlinCssVersion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-styled-next:$kotlinStyledNextVersion")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion:$kotlinEmotion")
            }
        }
    }
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

// include JS artifacts in any JAR we generate (set 'ORG_GRADLE_PROJECT_isProduction=true' as environment variable)
tasks.getByName<Jar>("jvmJar") {
    val taskName = if (project.hasProperty("isProduction") || project.gradle.startParameter.taskNames.contains("installDist")
    ) {
        "jsBrowserProductionWebpack"
    } else {
        "jsBrowserDevelopmentWebpack"
    }
    val webpackTask = tasks.getByName<KotlinWebpack>(taskName)
    dependsOn(webpackTask)
    from(File(webpackTask.destinationDirectory, webpackTask.outputFileName))
}

// include JS artifacts in any FAT JAR we generate
tasks.getByName<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    val taskName = if (project.hasProperty("isProduction") || project.gradle.startParameter.taskNames.contains("installDist")
    ) {
        "jsBrowserProductionWebpack"
    } else {
        "jsBrowserDevelopmentWebpack"
    }
    val webpackTask = tasks.getByName<KotlinWebpack>(taskName)
    dependsOn(webpackTask) // make sure JS gets compiled first
    from(File(webpackTask.destinationDirectory, webpackTask.outputFileName)) // bring output file along into the JAR
}

tasks {
    shadowJar {
        manifest.attributes.apply {
            put("Main-Class", "ServerKt")
        }
    }
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }
}

detekt {
    config = files("$projectDir/config/detekt.yml") // ... point to your custom config defining rules to run, overwriting default behavior
    baseline = file("$projectDir/config/baseline.xml") // ... a way of suppressing issues before introducing detekt
    source = files(
        "$projectDir/src/commonMain/kotlin",
        "$projectDir/src/jsMain/kotlin",
        "$projectDir/src/jvmMain/kotlin",
        "$projectDir/src/jvmTest/kotlin",
    )
}

distributions {
    main {
        contents {
            from("$buildDir/libs") {
                rename("${rootProject.name}-jvm", rootProject.name)
                into("lib")
            }
        }
    }
}

tasks.getByName<JavaExec>("run") {
    classpath(tasks.getByName<Jar>("jvmJar")) // ... so the JS artifacts generated by `jvmJar` can be found and served
}
