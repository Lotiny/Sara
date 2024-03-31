package me.lotiny.sara.utils

data class Replacement(
    var replacements: MutableMap<Any, Any> = HashMap(),
    var message: String = ""
) {

    /**
     * Create a `Replacement` object with the specified message.
     *
     * @param message The original message to which replacements will be applied.
     */
    constructor(message: String) : this() {
        this.message = message
    }

    /**
     * Add a replacement mapping to replace occurrences of `current` with `replacement`.
     *
     * @param current     The value to be replaced.
     * @param replacement The value to replace `current` with.
     */
    fun add(current: Any, replacement: Any) {
        replacements[current] = replacement
    }

    /**
     * Add all replacement mappings from another `Replacement` object to this one.
     *
     * @param replacement The `Replacement` object containing replacement mappings to be added.
     */
    fun addAll(replacement: Replacement) {
        replacements.putAll(replacement.replacements)
    }

    /**
     * Apply the replacements to the message and return the result as a translated string.
     *
     * @return The message with replacements applied and translated.
     */
    override fun toString(): String {
        replacements.keys.forEach { current ->
            message = message.replace(
                current.toString(),
                replacements[current].toString()
            )
        }
        return CC.translate(message)
    }
}
