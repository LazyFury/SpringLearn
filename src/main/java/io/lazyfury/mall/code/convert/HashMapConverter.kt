package io.lazyfury.mall.code.convert

import com.alibaba.fastjson.JSON
import jakarta.persistence.AttributeConverter

class HashMapConverter:AttributeConverter<HashMap<String, Any>,String> {
    override fun convertToDatabaseColumn(attribute: HashMap<String, Any>?): String {
       return JSON.toJSONString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): HashMap<String, Any> {
        if(dbData.isNullOrEmpty()){
            return  HashMap()
        }
        return JSON.parseObject(dbData, HashMap<String, Any>().javaClass)
    }
}