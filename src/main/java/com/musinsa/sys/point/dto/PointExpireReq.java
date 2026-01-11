package com.musinsa.sys.point.dto;

import lombok.Data;

@Data
public class PointExpireReq {
    Long uscoSno;
    Long mbrSno;
    String trDt;
    String trTm;
    String trAprvNo;
    String stlmWyDcmtNo;
    String ordNo;
    Long rqsAmt;
    Long pcsAmt;
}
