package com.musinsa.sys.point.repository;

import com.musinsa.sys.member.entity.Member;
import com.musinsa.sys.point.entity.PointLog;
import com.musinsa.sys.point.enums.PointLogType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PointLogRepository extends JpaRepository<PointLog, Long> {

    /**
     * 주문번호 기준 사용 승인 로그 단건 조회 (FOR UPDATE)
     */
    @Query(
            value = """
            SELECT *
            FROM point_log
            WHERE order_no = :orderNo
              AND log_type = :logType
            FOR UPDATE
        """,
            nativeQuery = true
    )
    PointLog findUseLogsByOrderNoForUpdate(
            @Param("orderNo") String orderNo,
            @Param("logType") String logType
    );

    /**
     * 주문번호 기준 사용 취소 누적 금액 조회
     */
    @Query(
            value = """
            SELECT COALESCE(SUM(pl.amount), 0)
            FROM point_log pl
            WHERE pl.order_no = :orderNo
              AND pl.log_type = :cancelType
        """,
            nativeQuery = true
    )
    long getCanceledAmount(
            @Param("orderNo") String orderNo,
            @Param("cancelType") String cancelType
    );
}


