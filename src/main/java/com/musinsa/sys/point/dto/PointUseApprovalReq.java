package com.musinsa.sys.point.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.musinsa.sys.point.enums.WalletSourceType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class PointUseApprovalReq  {

		@NotNull
		private Long memberId;

		@NotNull(message = "거래금액을 입력하세요.")
		private Long amount;

		@NotNull(message = "날짜데이터를 입력하세요.")
		@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
		private LocalDateTime logAt;

}
