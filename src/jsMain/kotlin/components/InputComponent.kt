package components

import BACKGROUND_COLOR
import PRIMARY_COLOR
import csstype.AlignItems
import csstype.Color
import csstype.Cursor
import csstype.Display
import csstype.FontSize
import csstype.FontWeight
import csstype.LineStyle
import csstype.NamedColor
import csstype.None
import csstype.pct
import csstype.px
import emotion.react.css
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLFormElement
import org.w3c.dom.HTMLInputElement
import react.FC
import react.Props
import react.dom.events.ChangeEventHandler
import react.dom.events.FormEventHandler
import react.dom.html.HTMLAttributes
import react.dom.html.InputHTMLAttributes
import react.dom.html.InputType
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.form
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.span
import react.useState

external interface InputProps : Props {
    var onSubmit: (String) -> Unit
}

/**
 * Represents the Input section of the UI
 */
val InputComponent = FC<InputProps> { props ->
    val (text, setText) = useState("")
    val submitHandler: FormEventHandler<HTMLFormElement> = {
        it.preventDefault()
        setText("")
        props.onSubmit(text)
    }
    val changeHandler: ChangeEventHandler<HTMLInputElement> = {
        setText(it.target.value)
    }

    div {
        inputComponentStyle()
        span {
            span { +"$ " }
            span {
                +"~/"

                css {
                    fontSize = FontSize.xLarge
                    marginLeft = 10.px
                }
            }
            css {
                display = Display.flex
                alignItems = AlignItems.center
                fontWeight = FontWeight.bold
                color = Color(PRIMARY_COLOR)
            }
        }
        span {
            css {
                display = Display.inlineBlock
                minWidth = 79.pct
            }
            form {
                onSubmit = submitHandler
                input {
                    formStyle()
                    type = InputType.text
                    onChange = changeHandler
                    value = text
                }
            }
        }
    }
}

private fun InputHTMLAttributes<HTMLInputElement>.formStyle() {
    css {
        border = None.none
        outline = None.none
        fontSize = FontSize.xxxLarge
        fontWeight = FontWeight.bold
        marginLeft = 10.px
        width = 100.pct
        background = Color(BACKGROUND_COLOR)
        color = Color(PRIMARY_COLOR)
        cursor = Cursor.pointer
        autoFocus = true
    }
}

private fun HTMLAttributes<HTMLDivElement>.inputComponentStyle() {
    css {
        color = NamedColor.black
        padding = 20.px
        fontSize = FontSize.xxxLarge
        display = Display.flex
        borderTop = LineStyle.solid
        borderBottom = LineStyle.solid
        borderLeft = LineStyle.hidden
        borderRight = LineStyle.hidden
        borderColor = Color(PRIMARY_COLOR)
    }
}
