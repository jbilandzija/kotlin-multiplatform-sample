import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class ApplicationTest {

    @Test
    fun `static resources are returned on root endpoint`() = testApplication {
        val outputJsFileName = "kt-multiplatform-sample.js"
        val response = client.get("/")

        with(response) {
            assertThat(this.status).isEqualTo(HttpStatusCode.OK)
            assertThat(this.headers["Content-Type"]).contains(ContentType.Text.Html.contentType, ContentType.Text.Html.contentSubtype)
            assertThat(this.bodyAsText()).contains(outputJsFileName)
        }
    }

    @Test
    fun `console endpoint returns expected object on empty input`() = testApplication {
        val output = Json.encodeToString(
            Output(value = "")
        )
        val response = client.get(API_ENDPOINT)

        with(response) {
            assertThat(this.status).isEqualTo(HttpStatusCode.OK)
            assertThat(this.headers["Content-Type"]).contains(ContentType.Application.Json.contentType, ContentType.Application.Json.contentSubtype)
            assertThat(this.bodyAsText()).isEqualTo(output)
        }
    }

    @Test
    fun `console endpoint returns expected object on help command`() = testApplication {
        val input = "help"
        val outputValue = "Kotlin Multiplatform sample - Usage is like a very very very simple command line"
        val response = client.get(API_ENDPOINT) {
            parameter(QUERY_PARAM, input)
        }

        with(response) {
            assertThat(this.status).isEqualTo(HttpStatusCode.OK)
            assertThat(this.headers["Content-Type"]).contains(ContentType.Application.Json.contentType, ContentType.Application.Json.contentSubtype)

            val responseObject = Json.decodeFromString<Output>(this.bodyAsText())
            assertThat(responseObject.value).contains(outputValue)
        }
    }
}
