package com.musinsa.sys.point.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum PointLogType {

    SAVING_APPROVAL("SA"),
    SAVING_CANCEL("SC"),
    USE_APPROVAL("UA"),
    USE_CANCEL("UC");

    private final String code;

    private static final Map<String, PointLogType> CODE_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(PointLogType::getCode, v -> v));
    public static PointLogType from(String code) {
        PointLogType type = CODE_MAP.get(code);
        if (type == null) {
            throw new IllegalArgumentException("Invalid PointLogType: " + code);
        }
        return type;
    }
}
