package adapters

import API_ENDPOINT
import QUERY_PARAM
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.browser.window
import Input
import Output

private val client = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

suspend fun getOutput(input: Input) = client.get(window.location.origin + API_ENDPOINT) {
    parameter(QUERY_PARAM, input.input)
}.body<Output>()
