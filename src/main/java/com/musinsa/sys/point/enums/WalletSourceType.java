package com.musinsa.sys.point.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WalletSourceType {
    MANUAL("MA"),
    AUTOMATIC("AU"),
    RESAVING("RE");

    private final String code;

    @JsonCreator
    public static WalletSourceType from(String code) {
        for (WalletSourceType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid WalletSourceType: " + code);
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}