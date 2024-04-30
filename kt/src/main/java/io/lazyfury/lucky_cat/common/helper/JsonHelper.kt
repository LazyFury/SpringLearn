package io.lazyfury.lucky_cat.common.helper

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONReader
import com.alibaba.fastjson2.JSONWriter
import com.alibaba.fastjson2.TypeReference

object JsonHelper {
    fun toMap(data: Any?): Map<String, Any> {
        val str = JSON.toJSONString(data, JSONWriter.Feature.WriteNulls)
        val map = JSON.parseObject(
            str,
            object : TypeReference<Map<String, Any>>() {},
            JSONReader.Feature.AllowUnQuotedFieldNames,
            JSONReader.Feature.Base64StringAsByteArray,
            JSONReader.Feature.SupportAutoType,
            JSONReader.Feature.SupportSmartMatch,
        )
        return map
    }
}