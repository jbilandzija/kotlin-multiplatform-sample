package components

import Output
import csstype.AlignItems
import csstype.Display
import csstype.FontFamily
import csstype.FontSize
import csstype.FontStyle
import csstype.None
import csstype.Padding
import csstype.WhiteSpace
import csstype.WordBreak
import csstype.px
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.code
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.span
import react.dom.html.ReactHTML.ul

external interface OutputProps : Props {
    var output: Output
    var lastInput: String
}

/**
 * Represents the Output section of the UI
 */
val OutputComponent = FC<OutputProps> { props ->
    ul {
        li {
            key = props.output.toString()
            code {
                span {
                    +"> "
                    css {
                        fontSize = FontSize.xxxLarge
                        marginRight = 10.px
                    }
                }
                span {
                    +props.lastInput
                    css {
                        fontSize = FontSize.xxLarge
                        fontStyle = FontStyle.italic
                    }
                }
                css {
                    display = Display.flex
                    alignItems = AlignItems.center
                }
            }
            code {
                +props.output.value
                css {
                    whiteSpace = WhiteSpace.breakSpaces
                    wordBreak = WordBreak.breakWord
                    fontSize = FontSize.xLarge
                }
            }
            css {
                padding = 20.px
            }
        }
        css {
            fontFamily = FontFamily.monospace
            listStyleType = None.none
            padding = Padding(0.px, 0.px)
        }
    }
}
