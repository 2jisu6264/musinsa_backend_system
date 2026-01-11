package com.musinsa.sys.common.enums;

import java.util.Arrays;

public enum ProcessCode {

    /* 공통 */
    HCO000("success", "HCO000", "정상으로 처리되었어요"),
    HCO001("fail", "HCO001", "올바르지 않은 거래구분코드입니다."),
    HCO002("fail", "HCO002", "올바르지 않은 거래상세구분코드입니다."),
    HCO003("fail", "HCO003", "1회 한도가 초가되었습니다."),
    HCO004("fail", "HCO004", "만료일은 최소 1일 이후여야 합니다."),
    HCO005("fail", "HCO005", "만료일은 5년 미만이어야 합니다."),
    HCO006("fail", "HCO006", "조회되는 거래가 없습니다."),
    HCO007("fail", "HCO007", "원거래 일시(originalLogAt)가 일치하지 않습니다."),
    HCO008("fail", "HCO008", "취소되었거나 만료된 거래 입니다."),
    HCO009("fail", "HCO009", "이미 사용되어 취소가 불가능한 거래 입니다."),
    HCO010("fail", "HCO010", "취소할 잔액이 부족합니다."),
    HCO011("fail", "HCO011", "지급 유형 코드가 잘못되었습니다."),
    HCO012("fail", "HCO011", "취소할 거래가 없습니다."),
    HCO998("fail", "HCO998", "요청 파라미터가 유효하지 않습니다."),
    HCO999("fail", "HCO999", "새로운 에러를 발견하셨어요. 고객센터 1600-4173(3번 하이픈페이)으로 연락해주세요"),


    /* 회원관리(Member) */
    HMB001("fail", "HMB001", "회원 정보를 찾을 수 없습니다."),
    HMB002("fail", "HMB002", "비밀번호가 맞지 않아요"),
    HMB003("fail", "HMB003", "회원 보유 잔액이 부족합니다.");


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
                .orElse(HCO999);
    }

}