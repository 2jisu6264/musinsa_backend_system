package com.musinsa.sys.point.dto;

import lombok.Data;

@Data
public class PointUseApprovalResp extends PointResp {
    private String orderNo;

    public PointUseApprovalResp(Long memberId, String orderNo, Long amount) {
        super(memberId, amount);
        this.orderNo = orderNo;
    }
}
