package com.musinsa.sys.common.enums;

import java.util.Arrays;

public enum ProcessCode {

    /* 공통 */
    MP000("success", "MP000", "정상으로 처리되었어요"),
    MP001("fail", "MP001", "올바르지 않은 거래구분코드입니다."),
    MP002("fail", "MP002", "포인트는 1원 이상부터 적립이 가능합니다. "),
    MP003("fail", "MP003", "1회 한도가 초과되었습니다."),
    MP004("fail", "MP004", "만료일은 최소 1일 이후여야 합니다."),
    MP005("fail", "MP005", "만료일은 5년 미만이어야 합니다."),
    MP006("fail", "MP006", "조회되는 거래가 없습니다."),
    MP007("fail", "MP007", "원거래 일시(originalLogAt)가 일치하지 않습니다."),
    MP008("fail", "MP008", "취소되었거나 만료된 거래 입니다."),
    MP009("fail", "MP009", "이미 사용되어 취소가 불가능한 거래 입니다."),
    MP010("fail", "MP010", "잔액이 부족합니다."),
    MP011("fail", "MP011", "지급 유형 코드가 잘못되었습니다."),
    MP012("fail", "MP012", "취소할 거래가 없습니다."),
    MP013("fail", "MP013", "사용승인 금액보다 사용취소 금액이 더 큽니다."),
    MP998("fail", "MP998", "요청 파라미터가 유효하지 않습니다."),
    MP999("fail", "MP999", "새로운 에러를 발견하셨어요. 고객센터로 연락해주세요"),

    /* 회원관리(Member) */
    HB001("fail", "HB001", "회원 정보를 찾을 수 없습니다.");

    private String sucsFalr;
    private String procCd;
    private String rsltMesg;

    private ProcessCode(String sucsFalr, String procCd, String rsltMesg) {
        this.sucsFalr = sucsFalr;
        this.procCd = procCd;
        this.rsltMesg = rsltMesg;
    }

    public String getSucsFalr() {
        return sucsFalr;
    }

    public String getProcCd() {
        return procCd;
    }

    public String getRsltMesg() {
        return rsltMesg;
    }

    public static ProcessCode findByProcessCode(String inProcCd) {
        return Arrays.stream(ProcessCode.values())
                .filter(procCd -> procCd.getProcCd().equals(inProcCd))
                .findAny()
                .orElse(MP999);
    }

}