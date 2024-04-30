package io.lazyfury.lucky_cat.common.reponse

import com.fasterxml.jackson.annotation.JsonValue

class ApiJsonResponse<T>(
    val err: ErrCode,
    val message: String = "",
    val data: T? = null
) {
    companion object {
        fun <T> success(data: T? = null): ApiJsonResponse<T> {
            return ApiJsonResponse<T>(ErrCode.SUCCESS, "", data)
        }

        fun <T> success(data: T? = null, message: String = ""): ApiJsonResponse<T> {
            return ApiJsonResponse<T>(ErrCode.SUCCESS, message, data)
        }

        fun <T> error(code: ErrCode = ErrCode.ERROR, message: String = ""): ApiJsonResponse<T> {
            return ApiJsonResponse<T>(code, message)
        }
    }

    @JsonValue
    fun toValue(): Map<String, Any?> {
        return mapOf(
            "code" to err.code,
            "label" to err.label,
            "message" to (message.ifBlank { err.message }),
            "data" to data
        )
    }
}
