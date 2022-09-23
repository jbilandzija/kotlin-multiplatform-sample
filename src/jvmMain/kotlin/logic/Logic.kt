package logic

import Commands
import Output
import com.github.ricksbrown.cowsay.plugin.CowExecutor
import HELP_OUTPUT
import KT_MP_OUTPUT
import KT_OUTPUT
import toCommand

/* Logic consists only of a simple mapping. */
fun getOutput(input: String) = when (input.toCommand()) {
    Commands.EMPTY -> Output("")
    Commands.UNKNOWN -> notFound()
    Commands.HELP -> Output(HELP_OUTPUT)
    Commands.KOTLIN -> Output(KT_OUTPUT)
    Commands.KOTLIN_MP -> Output(KT_MP_OUTPUT)
}

fun notFound() = Output(createCowSay("Command not recognized. Try 'help' for assistance."))

fun createCowSay(message: String): String = with(CowExecutor()) {
    this.setMessage(message)
    this.setCowfile("cow")
    this
}.execute()
