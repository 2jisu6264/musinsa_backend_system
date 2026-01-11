package com.musinsa.sys.order.component;

import com.musinsa.sys.order.entity.OrderSequenceLog;
import com.musinsa.sys.order.repository.OrderSequenceRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class OrderNoGenerator {

    private final OrderSequenceRepository orderSeqRepository;

    public OrderNoGenerator(OrderSequenceRepository orderSeqRepository) {
        this.orderSeqRepository = orderSeqRepository;
    }

    /**
     * 주문번호 생성
     * yyyyMMddHHmmss + 4자리 시퀀스
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generateOrderNo() {

        LocalDateTime now = LocalDateTime.now().withNano(0);

        String orderDateTime = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        OrderSequenceLog orderSequenceLog = orderSeqRepository.findForUpdate(orderDateTime)
                .orElseGet(() -> new OrderSequenceLog(orderDateTime));

        orderSequenceLog.increase();

        orderSeqRepository.save(orderSequenceLog);

        return orderDateTime + String.format("%04d", orderSequenceLog.getSeq());
    }

}
