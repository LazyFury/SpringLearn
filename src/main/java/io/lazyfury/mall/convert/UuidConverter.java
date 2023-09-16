package io.lazyfury.mall.convert;

import jakarta.persistence.AttributeConverter;

import java.util.UUID;

public class UuidConverter implements AttributeConverter<UUID, String> {

    @Override
    public String convertToDatabaseColumn(UUID attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.toString();
    }

    @Override
    public UUID convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return UUID.fromString(dbData);
    }
}
