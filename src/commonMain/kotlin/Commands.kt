enum class Commands(
    val input: String,
) {
    /* Hard coded Input-Output mapping. Sufficient for this basic prototyping. */
    HELP("help"),
    KOTLIN("kotlin"),
    KOTLIN_MP("kotlin:mp"),
    EMPTY(""),
    UNKNOWN("unknown");
}

private fun commandByInput() = Commands.values().associateBy { it.input }
fun String.toCommand() = commandByInput()[this] ?: Commands.UNKNOWN
