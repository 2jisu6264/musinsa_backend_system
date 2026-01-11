package com.musinsa.sys.common.util;

import java.util.Map;
import java.util.Optional;

public class MapUtil {
    /**
     * Map에 있는 특정 value 꺼내 쓰는 Util -> Key가 존재하면 value 리턴, 없으면 defaultValue 리턴
     */
    public static String getMapValue(Map<String, Object> map, String key, String defaultValue) {
        if (map == null)
            return defaultValue;

        return Optional.ofNullable(map.get(key))
                .map(String::valueOf)
                .orElse(defaultValue);
    }

}
