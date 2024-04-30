package io.lazyfury.lucky_cat.common.utils.validate

abstract class Rule(
    val message: String
) {
    open fun validate(key: String, data: Map<String, Any?>) {
        throw IllegalArgumentException("Not implemented.")
    }

    class Required(
        message: String,
    ) : Rule(
        message = message
    ) {
        override fun validate(key: String, data: Map<String, Any?>) {
            val value = data[key] ?: throw IllegalArgumentException("${key} is required")
            if (value is String && value.isBlank()) {
                throw IllegalArgumentException("${key} is required")
            }
        }
    }
}
