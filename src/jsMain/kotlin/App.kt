import adapters.getOutput
import components.InputComponent
import components.OutputComponent
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.FC
import react.Props
import react.dom.html.ReactHTML.h1
import react.useEffectOnce
import react.useState

// Coroutine scope for UI elements
private val scope = MainScope()

val App = FC<Props> {
    var output by useState(Output(""))
    var lastInput by useState("")

    useEffectOnce {
        // Launch a new non-blocking coroutine scope
        scope.launch {
            output = Output("")
        }
    }

    h1 { +"Kotlin Multiplatform Sample - 'help' for assistance" }

    InputComponent {
        onSubmit = { input ->
            scope.launch {
                lastInput = input
                getOutput(input = Input(input)).also {
                    output = it
                }
            }
        }
    }

    OutputComponent {
        this.output = output
        this.lastInput = lastInput
    }
}
