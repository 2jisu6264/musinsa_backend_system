package com.musinsa.sys.point.entity;

import com.musinsa.sys.point.enums.WalletSourceType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class WalletSourceTypeConverter
        implements AttributeConverter<WalletSourceType, String> {

    @Override
    public String convertToDatabaseColumn(WalletSourceType attribute) {
        return attribute == null ? null : attribute.getCode(); // MA, AU
    }

    @Override
    public WalletSourceType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : WalletSourceType.from(dbData);
    }
}
