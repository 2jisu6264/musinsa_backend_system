package com.musinsa.sys.point.repository;

import com.musinsa.sys.point.entity.PointUseDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointUseDetailRepository extends JpaRepository<PointUseDetail, Long> {

}

