import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain.main
import plugins.configureContentNegotiation
import plugins.configureMonitoring
import plugins.configureRouting

fun main(args: Array<String>): Unit = main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureContentNegotiation()
    configureMonitoring()
    configureRouting()
}
