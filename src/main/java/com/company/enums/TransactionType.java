package com.company.enums;

public enum TransactionType {

    credit, debit;

    public static TransactionType getEnum(String value) {
        if (null == value)
            return null;
        return TransactionType.valueOf(value);
    }


    public static String getStringValue(TransactionType type) {
        if (null == type)
            return null;
        return type.name();
    }

    }
