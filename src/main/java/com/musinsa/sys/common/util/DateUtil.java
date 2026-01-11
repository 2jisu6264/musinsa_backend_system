package com.musinsa.sys.common.util;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
public class DateUtil {
    /**
     * 현재 일자 시간을 리턴한다.
     *
     * @param format_str - yyyy MM dd HH mm ss SSS
     * @return Current Date time - strNow
     */
    public static String getCurDtim(String format_str) {
        long now = System.currentTimeMillis();
        SimpleDateFormat sdfNow = new SimpleDateFormat(format_str);
        String strNow = sdfNow.format(new Date(now));
        return strNow;
    }


    public static LocalDateTime getLocalDateTimeWithNano() {
        return LocalDateTime.now().withNano(0);
    }

}
