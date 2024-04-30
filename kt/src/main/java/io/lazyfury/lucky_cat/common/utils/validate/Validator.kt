package io.lazyfury.lucky_cat.common.utils.validate

import io.lazyfury.lucky_cat.common.helper.JsonHelper


class Validator(
    private val rules: Map<String,List<Rule>>
) {

    fun validate(data: Any?) {
        val params = JsonHelper.toMap(data)
        validate(params)
    }

    fun validate(data: Map<String, Any?>) {
        print("Validating data: $data")
        rules.forEach { (key, rule) ->
            rule.forEach {
                try{
                    it.validate(key, data)
                }catch (e: IllegalArgumentException){
                    throw IllegalArgumentException("Validation failed for $key: ${it.message}")
                }
            }
        }
    }
}