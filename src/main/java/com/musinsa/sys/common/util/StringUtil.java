package com.musinsa.sys.common.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class StringUtil {

    /**
     * StackTrace 를 String 으로 변환하는 메소드
     *
     * @param e Exception
     * @return String
     */
    public static String getStackTraceToString(Exception e) {
        ByteArrayOutputStream ostr = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(ostr));
        return (ostr.toString());
    }

}
