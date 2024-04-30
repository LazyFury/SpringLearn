package io.lazyfury.lucky_cat.common.reponse

import com.fasterxml.jackson.annotation.JsonValue

enum class ErrCode(val code: Int,  val label:String, val message: String) {
    SUCCESS(200,"success", "请求成功"),
    ERROR(500,"error", "error"),
    NOT_FOUND(404,"not_found", "not found"),
    UNAUTHORIZED(401,"unauthorized", "unauthorized"),
    FORBIDDEN(403,"forbidden", "forbidden"),
    BAD_REQUEST(400,"bad_request", "bad request"),
    METHOD_NOT_ALLOWED(405,"method_not_allowed", "method not allowed"),
    CONFLICT(409,"conflict", "conflict"),
    INTERNAL_SERVER_ERROR(500,"internal_server_error", "internal server error"),
    SERVICE_UNAVAILABLE(503,"service_unavailable", "service unavailable"),
    GATEWAY_TIMEOUT(504,"gateway_timeout", "gateway timeout"),
    ;

    @JsonValue
    fun toValue(): Map<String, Any> {
        return mapOf(
            "code" to code,
            "message" to message
        )
    }
}