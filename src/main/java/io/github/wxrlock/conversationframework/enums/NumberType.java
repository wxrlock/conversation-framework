package io.github.wxrlock.conversationframework.enums;

import lombok.Getter;

@Getter
public enum NumberType {

    NONE("texto"),
    INTEGER("número inteiro"),
    FLOAT("número"),
    DOUBLE("número");

    private final String description;

    NumberType(String description) {
        this.description = description;
    }

}