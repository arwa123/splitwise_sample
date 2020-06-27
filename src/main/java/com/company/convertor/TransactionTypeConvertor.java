package com.company.convertor;


import com.company.enums.TransactionType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TransactionTypeConvertor implements AttributeConverter<TransactionType, String> {


    @Override
    public String convertToDatabaseColumn(TransactionType type) {

        return TransactionType.getStringValue(type);
    }

    @Override
    public TransactionType convertToEntityAttribute(String dbData) {

        return TransactionType.getEnum(dbData);
    }

}