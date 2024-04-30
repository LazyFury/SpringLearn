package io.lazyfury.lucky_cat.client.api

import io.lazyfury.lucky_cat.ai.translate.DifyTranslate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TranslateApi(
    private val difyTranslate: DifyTranslate,
) {

    @PostMapping("/translate")
    fun translate(
        @RequestBody
        params: DifyTranslate.TranslateParams): String? {
        println(params)
        return difyTranslate.translate(params)
    }
}