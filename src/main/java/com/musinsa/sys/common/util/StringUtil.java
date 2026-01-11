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

    /**
     * 파라미터의 값이 null일때 초기화, null이 아닌경우 trim
     *
     * @param param 대상 문자열
     * @return String
     */
    public static String nullTrim(String param) {

        if (param == null) {
            param = "";
            return param;
        } else if (param.equals("null")) {
            param = "";
            return param;
        } else {
            return param.trim();
        }
    }

    /**
     * 파라미터의 값이 null일때 초기화, null이 아닌경우 trim
     *
     * @param param 대상 문자열
     * @return String
     */
    public static String nullTrim(Long param) {

        String retrunStr = "";
        if (param == null) {
            retrunStr = "";
            return retrunStr;
        } else {
            return Long.toString(param).trim();
        }
    }

    /**
     * Null 문자열 ("NULL,null") 이 들어올 경우 널 스트링을 리턴하는 함수
     *
     * @param data 판별대상 문자열
     * @return 널스트링
     */
    public static String nullCheck(String data) {
        if (data == null || data.equals("null") || data.equals("NULL")) {
            data = "";
        }
        return data;
    }


    /**
     * Null 문자열 ("NULL,null") 이 들어올 경우 스페이스 한칸을 리턴하는 함수
     *
     * @param data 판별대상 문자열
     * @return 널스트링
     */
    public static String nullToSpace(String data) {
        if (data == null) {
            data = " ";
        } else if (data.equals("null") || data.equals("NULL") || data.equals("")) {
            data = " ";
        }
        return data;
    }

    /**
     * 입력된 문자를 지정된 길이의 전문형태로 변환한다. <br/>
     * 지정된 길이(len)보다 입력된 문자(str)이 큰 문자일 경우 지정된 길이 만큼만 리턴한다. <br/>
     * 한글을 substring할 경우 StringIndexOutOfBoundsException이 발생하며 이때 모든 문자열을 '?'로 처리한다.<br/>
     *  - type 정의 (입력된 문자(str)의 길이가 지정된 길이(len) 보다 작을 때 처리) <br/>
     *      SL : 남은 길이(len-str길이)만큼 앞쪽에 공백(" ")을 붙임 <br/>
     *      SR : 남은 길이(len-str길이)만큼 뒤쪽에 공백(" ")을 붙임 <br/>
     *      0L : 남은 길이(len-str길이)만큼 앞쪽에 공백("0")을 붙임 <br/>
     *      0R : 남은 길이(len-str길이)만큼 뒤쪽에 공백("0")을 붙임 <br/>
     * @param type : SL, SR, 0L, 0R
     * @param str : 대상문자
     * @param len : 지정길이
     * @return
     */
    public static String fitToLength(String type, String str, int len) {

        StringBuffer sb = new StringBuffer();
        byte[] buff;
        int i, filler_len;

        if( str == null ) str = "";

        /* 길이가 overflow발생할 경우 문자열을(#)으로 초기화 */
        if( str.getBytes().length > len ) str = "#";

        buff = str.getBytes();
        filler_len = len - buff.length;

        if(filler_len == 0){
            sb.append(str);
        }else{
            if(filler_len > 0){
                if(type.equals("SR")){
                    sb.append(str);
                    for(i=0;i<filler_len;i++){ sb.append(" "); }
                }else if(type.equals("SL")){
                    for(i=0;i<filler_len;i++){ sb.append(" "); }
                    sb.append(str);
                }else if(type.equals("0R")){
                    sb.append(str);
                    for(i=0;i<filler_len;i++){ sb.append("0"); }
                }else if(type.equals("0L")){
                    for(i=0;i<filler_len;i++){ sb.append("0"); }
                    sb.append(str);
                }
            }else{
                try{
                    sb.append(str.substring(0, len));
                }catch(StringIndexOutOfBoundsException e){
                    for(i=0;i<len;i++){ sb.append("?"); }
                }
            }
        }

        return sb.toString();
    }

}
