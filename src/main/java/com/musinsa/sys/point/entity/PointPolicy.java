package com.musinsa.sys.point.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "point_policy")
public class PointPolicy {

    @Id
    @Column(name = "policy_key", length = 50)
    private String policyKey;

    @Column(name = "policy_value", nullable = false)
    private Long policyValue;

    @Column(name = "description", length = 200)
    private String description;
}
