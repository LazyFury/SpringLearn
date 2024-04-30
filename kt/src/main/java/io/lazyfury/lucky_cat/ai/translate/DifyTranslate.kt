package io.lazyfury.lucky_cat.ai.translate

import com.alibaba.fastjson2.JSON
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class DifyTranslate(
) {

    @Value("\${dify.apiKey}")
    private lateinit var apiKey: String

    @Value("\${dify.url}")
    private lateinit var difyUrl: String

    private fun headers(map: Map<String, String>? = null): Map<String, String> {
        return mapOf(
            "Authorization" to "Bearer $apiKey",
            "Content-Type" to "application/json",
            *map?.map { (key, value) ->
                key to value
            }?.toTypedArray() ?: arrayOf()
        )
    }

    data class TranslateParams(
        val content: String,
        val from: String,
        val to: String,
        val user:String = "1",
        val conversationId:String? = null
    )


    //    response_mode
    enum class ResponseMode(val value: String) {
        STREAMING("streaming"),
        BLOCKING("blocking")
    }

    /**
     * Translate text
     * @param params
     * @param userId
     * @param conversationId
     * @param mode
     * @return HttpResponse<JsonNode>?
     */
    fun translate(
        params: TranslateParams,
        mode: ResponseMode = ResponseMode.BLOCKING
    ): String? {
        val data = mapOf(
            "inputs" to mapOf(
                "content" to params.content,
                "from" to params.from,
                "to" to params.to
            ),
            "response_mode" to mode.value,
            "user" to params.user,
            "conversation_id" to params.conversationId
        )
        println("request translate $data")
        val url = "$difyUrl/completion-messages"
        println("url $url")
        val response = Unirest.post(url).headers(
            headers()
        )

            .body(
                JSON.toJSONString(data)
            )
            .asString()

        return response.body
    }
}