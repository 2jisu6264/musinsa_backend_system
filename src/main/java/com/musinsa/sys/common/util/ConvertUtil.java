package com.musinsa.sys.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ConvertUtil {
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        // Java 8 날짜/시간(LocalDateTime) 지원
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * 객체 간 변환 (예: DTO -> Entity, Object -> Map)
     */
    public static <T, D> D convertTo(T from, Class<D> toDataType) {
        if (from == null)
            return null;
        return objectMapper.convertValue(from, toDataType);
    }

    /**
     * JSON 문자열 -> 객체 변환
     */
    public static <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
        if (json == null || json.isBlank())
            return null;

        return objectMapper.readValue(json, clazz);
    }

    /**
     * 객체 -> JSON 문자열 변환
     */
    public static String toJson(Object object) throws JsonProcessingException {
        if (object == null)
            return null;

        return objectMapper.writeValueAsString(object);
    }


}
