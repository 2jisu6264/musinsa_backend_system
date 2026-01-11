package com.musinsa.sys.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "order_sequence_log")
public class OrderSequenceLog {

    @Id
    @Column(name = "order_date", length = 6)
    private String orderDate;

    @Column(name = "seq", nullable = false)
    private int seq;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public void increase() {
        this.seq++;
    }
    public OrderSequenceLog(String orderDate) {
        this.orderDate = orderDate;
        this.seq = 0000;
        this.createdAt = LocalDateTime.now();
    }
}
