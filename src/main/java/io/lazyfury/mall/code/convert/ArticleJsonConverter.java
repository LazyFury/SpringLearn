package io.lazyfury.mall.code.convert;

import com.alibaba.fastjson.JSON;
import io.lazyfury.mall.code.entity.ArticleJsonConfig;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

@Component
@Converter(autoApply = true)
public  class ArticleJsonConverter implements AttributeConverter<ArticleJsonConfig, String> {
    @Override
    public String convertToDatabaseColumn(ArticleJsonConfig attribute) {
        return JSON.toJSONString(attribute);
    }

    @Override
    public ArticleJsonConfig convertToEntityAttribute(String dbData) {
        return JSON.parseObject(dbData,ArticleJsonConfig.class);
    }
}
