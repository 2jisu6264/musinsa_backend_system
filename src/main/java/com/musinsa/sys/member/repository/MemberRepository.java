package com.musinsa.sys.member.repository;

import com.musinsa.sys.member.entity.Member;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from member m where m.memberId = :memberId")
    Member findByMemberIdForUpdate(@Param("memberId") Long memberId);

    Member findByMemberId(Long memberId);


}

