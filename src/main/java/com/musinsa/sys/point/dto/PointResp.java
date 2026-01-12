package com.musinsa.sys.point.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
