package com.company.enums;

public enum ExpenseType {

    PERCENTAGE, EQUAL, EXACT;


    public static ExpenseType getEnum(String value) {
        if (null == value)
            return null;
        return ExpenseType.valueOf(value);
    }


    public static String getStringValue(ExpenseType type) {
        if (null == type)
            return null;
        return type.name();
    }

}
