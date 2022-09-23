package plugins

import API_ENDPOINT
import QUERY_PARAM
import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import logic.getOutput

fun Application.configureRouting() {
    routing {
        route(API_ENDPOINT) {
            get {
                val input = call.request.queryParameters[QUERY_PARAM] ?: ""
                val output = getOutput(input)

                call.respond(output)
            }
        }

        // SERVE FRONTEND
        get("/") {
            call.respondText(
                this::class.java.classLoader.getResource("index.html")?.readText() ?: error("Failure loading index.html!"),
                ContentType.Text.Html
            )
        }

        static("/") {
            resources("")
        }
    }
}
