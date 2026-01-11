package com.musinsa.sys.point.repository;

import com.musinsa.sys.point.entity.PointWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointWalletRepository extends JpaRepository<PointWallet, Long> {
    PointWallet findByMemberIdAndWalletId(Long memberId, Long walletId);

    @Query(value =
            "SELECT * " +
                    "FROM point_wallet " +
                    "WHERE wallet_status = '00' " +
                    "  AND issued_amount != used_amount " +
                    "  AND member_id = :memberId " +
                    "ORDER BY " +
                    "  CASE WHEN source_type = 'MA' THEN 0 ELSE 1 END ASC, " +
                    "  expire_date ASC",
            nativeQuery = true
    )
    List<PointWallet> findUsableWallets(@Param("memberId") Long memberId);

    @Query(value = "select * "
            + "from point_wallet "
            + "WHERE wallet_status = '00' "
            + "AND used_amount != 0 "
            + "AND member_id = :memberId "
            + "ORDER BY expire_date desc ", nativeQuery = true)
    public List<PointWallet> findCancelWallets(@Param("memberId") Long memberId);

}

