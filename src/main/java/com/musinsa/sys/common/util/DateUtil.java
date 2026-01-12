package com.musinsa.sys.common.util;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
public class DateUtil {

    public static LocalDateTime getLocalDateTimeWithNano() {
        return LocalDateTime.now().withNano(0);
    }
    public static LocalDate resolveExpireDate(
            LocalDate expireDate,
            LocalDateTime logAt
    ) {
        if (expireDate != null) {
            return expireDate;
        }

        return logAt.toLocalDate().plusYears(1);
    }

}
