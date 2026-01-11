package com.musinsa.sys.point.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.musinsa.sys.point.enums.WalletSourceType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class PointSavingApprovalReq  {

	@NotNull
	private Long memberId;

	@NotNull(message = "지급 유형값을 입력하세요.")
	private WalletSourceType sourceType;

	@NotNull(message = "거래금액을 입력하세요.")
	private Long amount;

	@NotNull
	private LocalDateTime logAt;

	@NotNull(message = "포인트만료일을 입력하세요.")
	@JsonFormat(pattern = "yyyyMMdd")
	private LocalDate  expireDate;
}
