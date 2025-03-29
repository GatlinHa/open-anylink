package com.hibob.anylink.mts.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ObjectType {
    IMAGE(0),
    AUDIO(1),
    VIDEO(2),
    DOCUMENT(3),
    DEFAULT(99),
    ;

    private final int value;

    public int value() {
        return value;
    }
}
