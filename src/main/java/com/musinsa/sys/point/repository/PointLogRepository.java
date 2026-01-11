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

    // 회원별 거래 내역 조회
    PointLog findByMemberId(Long memberId);

    PointLog findByOrderNo(String orderNo);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from point_log p where p.orderNo = :orderNo AND p.logType = :logType")
    PointLog findUseLogsByOrderNoForUpdate(@Param("orderNo") String orderNo, @Param("logType") String logType);

}

