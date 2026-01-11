package com.musinsa.sys.order.repository;

import com.musinsa.sys.order.entity.OrderSequenceLog;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderSequenceRepository extends JpaRepository<OrderSequenceLog, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    select o
    from order_sequence_log o
    where o.orderDate = :orderDateTime
""")
    Optional<OrderSequenceLog> findForUpdate(
            @Param("orderDateTime") String orderDateTime
    );
}
