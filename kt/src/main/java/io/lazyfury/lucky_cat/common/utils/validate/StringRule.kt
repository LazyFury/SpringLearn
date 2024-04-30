package io.lazyfury.lucky_cat.common.utils.validate

object StringRule {


    class Min (
        message: String,
        private val minLength: Int
    ): Rule(
        message=message
    ) {
        override fun validate(key: String, data: Map<String, Any?>) {
            val value = data[key]
            if (value is String) {
                if (value.length < minLength) {
                    throw IllegalArgumentException("${key} is too short")
                }
            }
        }
    }

    class Max (
        message: String,
        private val maxLength: Int
    ): Rule(
        message=message
    ) {
        override fun validate(key: String, data: Map<String, Any?>) {
            val value = data[key]
            if (value is String) {
                if (value.length > maxLength) {
                    throw IllegalArgumentException("${key} is too long")
                }
            }
        }
    }

    class Pattern(
        message: String,
        private val pattern: java.util.regex.Pattern
    ): Rule(
        message=message
    ) {
        override fun validate(key: String, data: Map<String, Any?>) {
            val value = data[key]
            if (value is String) {
                if (!pattern.matcher(value).matches()) {
                    throw IllegalArgumentException("${key} is invalid")
                }
            }
        }
    }
}