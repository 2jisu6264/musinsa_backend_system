package com.musinsa.sys.point.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PointExpireResp {
    Long uscoSno;
    Long mbrSno;
    String trDt;
    String trTm;
    String trAprvNo;
    String ordNo;
    Long rqsAmt;
    Long pcsAmt;
}
