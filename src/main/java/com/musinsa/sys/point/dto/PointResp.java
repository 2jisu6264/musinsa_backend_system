package com.musinsa.sys.point.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
/* 포인트 적립, 적립취소 ,사용승인, 사용취소, 개인지급, 개인지급취소 Resp */
public class PointResp {
	private Long memberId;
	private Long amount;

    public PointResp(Long memberId) {
        this.memberId = memberId;
    }

    public PointResp(Long memberId, Long amount) {
        this(memberId);
        this.amount = amount;
    }


}
