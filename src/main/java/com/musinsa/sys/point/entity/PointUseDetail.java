package com.musinsa.sys.point.entity;

import com.musinsa.sys.common.util.DateUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "point_use_detail")
public class PointUseDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "use_id")
    private Long useId;

    @Column(name = "order_no", nullable = false)
    private String orderNo;

    @Column(name = "used_amount", nullable = false)
    private Long usedAmount;

    @Column(name = "created_at", length = 8, nullable = false)
    private LocalDateTime createdAt;

    public static PointUseDetail from(PointLog pointLog) {
        return PointUseDetail.builder()
                .orderNo(pointLog.getOrderNo())
                .usedAmount(pointLog.getAmount())
                .createdAt(DateUtil.getLocalDateTimeWithNano())
                .build();

    }
}
