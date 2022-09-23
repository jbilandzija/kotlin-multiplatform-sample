import csstype.FontFamily
import kotlinx.browser.document
import kotlinx.css.Color
import kotlinx.css.CssBuilder
import kotlinx.css.backgroundColor
import kotlinx.css.body
import kotlinx.css.color
import kotlinx.css.fontFamily
import react.create
import react.dom.client.createRoot
import styled.injectGlobal

fun main() {
    val container = document.getElementById("root") ?: error("Unable to find root element!")
    val styles = CssBuilder(allowClasses = false).apply {
        body {
            backgroundColor = Color(BACKGROUND_COLOR)
            color = Color(PRIMARY_COLOR)
            fontFamily = FontFamily.monospace.toString()
        }
    }
    injectGlobal(styles)

    createRoot(container).render(App.create())
}
