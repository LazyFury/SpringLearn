package io.lazyfury.lucky_cat.common.utils.validate

object NumberRule {

    class NotZero(
        message: String
    ): Rule(
        message=message
    ) {
        override fun validate(key: String, data: Map<String, Any?>) {
            val value = data[key]
            if (value is Number) {
                if (value == 0) {
                    throw IllegalArgumentException("${key} is zero")
                }
            }
        }
    }

}