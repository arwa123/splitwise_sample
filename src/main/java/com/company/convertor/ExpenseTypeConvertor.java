package com.company.convertor;

import com.company.enums.ExpenseType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ExpenseTypeConvertor implements AttributeConverter<ExpenseType, String> {


    @Override
    public String convertToDatabaseColumn(ExpenseType type) {

        return ExpenseType.getStringValue(type);
    }

    @Override
    public ExpenseType convertToEntityAttribute(String dbData) {

        return ExpenseType.getEnum(dbData);
    }

}