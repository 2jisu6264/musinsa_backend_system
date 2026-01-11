package com.musinsa.sys.point.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PointSavingCancelReq  {
	@NotNull(message = "회원ID를 입력하세요.")
	private Long memberId;

	@NotNull(message = "금액을 입력하세요.")
	private Long amount;

	@NotNull(message = "취소거래 Id를 입력하세요.")
	private Long walletId;

	@NotNull(message = "거래일시를 입력하세요.")
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime logAt;

}
