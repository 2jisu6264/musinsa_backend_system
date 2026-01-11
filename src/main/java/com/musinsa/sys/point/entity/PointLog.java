package com.musinsa.sys.point.entity;

import com.musinsa.sys.common.util.DateUtil;
import com.musinsa.sys.point.enums.PointLogType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "point_log")
public class PointLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "log_type", length = 2, nullable = false)
    private String logType;

    @Column(name = "log_at", length = 8, nullable = false)
    private LocalDateTime logAt;

    @Column(name = "order_no", length = 20)
    private String orderNo;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static PointLog from(Long memberId, Long amount, String pointLogType, LocalDateTime logAt) {
        return PointLog.builder()
                .logType(pointLogType)
                .memberId(memberId)
                .amount(amount)
                .logAt(logAt)
                .createdAt(DateUtil.getLocalDateTimeWithNano())
                .build();
    }

}
