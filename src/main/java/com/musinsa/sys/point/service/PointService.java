package com.musinsa.sys.point.service;

import com.musinsa.sys.common.constants.Val;
import com.musinsa.sys.common.enums.ProcessCode;
import com.musinsa.sys.common.exception.ServiceException;
import com.musinsa.sys.common.util.DateUtil;
import com.musinsa.sys.member.entity.Member;
import com.musinsa.sys.member.repository.MemberRepository;
import com.musinsa.sys.order.component.OrderNoGenerator;
import com.musinsa.sys.point.dto.*;
import com.musinsa.sys.point.entity.PointLog;
import com.musinsa.sys.point.entity.PointPolicy;
import com.musinsa.sys.point.entity.PointUseDetail;
import com.musinsa.sys.point.entity.PointWallet;
import com.musinsa.sys.point.enums.PointLogType;
import com.musinsa.sys.point.enums.PointPolicyKey;
import com.musinsa.sys.point.enums.WalletSourceType;
import com.musinsa.sys.point.repository.PointLogRepository;
import com.musinsa.sys.point.repository.PointPolicyRepository;
import com.musinsa.sys.point.repository.PointUseDetailRepository;
import com.musinsa.sys.point.repository.PointWalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 포인트 적립 / 적립취소 / 사용 / 사용취소에 대한
 * Service
 * <p>
 * - 트랜잭션 단위로 포인트 상태를 일관되게 관리
 * - Member(회원 잔액), PointWallet(원장), PointLog(이력)을 함께 처리
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PointService {

    private final PointLogRepository pointLogRepository;
    private final PointPolicyRepository pointPolicyRepository;
    private final PointWalletRepository pointWalletRepository;
    private final OrderNoGenerator orderNoGenerator;
    private final MemberRepository memberRepository;
    private final PointUseDetailRepository pointUseDetailRepository;

    /**
     * 포인트 적립 승인
     * <p>
     * 정책 검증 → 로그 생성 → 회원 잔액 증가 → 포인트 지갑 생성
     */
    @Transactional
    public PointResp savingApproval(PointSavingApprovalReq pointSavingApprovalReq) {

        Long memberId = pointSavingApprovalReq.getMemberId();
        Long amount = pointSavingApprovalReq.getAmount();
        LocalDateTime logAt = pointSavingApprovalReq.getLogAt();

        PointLog pointLog = new PointLog();

        //거래구분코드 확인
        pointLog.setLogType(PointLogType.SAVING_APPROVAL.getCode());

        //만료일 미입력시 기본 1년 셋팅
        LocalDate expireDate =
                DateUtil.resolveExpireDate(pointSavingApprovalReq.getExpireDate(), pointSavingApprovalReq.getLogAt());

        // 회원 조회 + 락 (동시성 제어)
        Member member = getMember(memberId);
        // 적립 정책 검증 ( 1회 충전금액, 총보유금액, 만료일)
        validateSavingAmount(amount);                 // 1회 적립 금액 제한
        validateBalanceLimit(member.getPointBalance(), amount); // 총 보유 한도
        validateExpireDate(expireDate); // 만료일 범위 검증

        pointLog.setAmount(amount);
        pointLog.setLogAt(logAt);

        // 적립승인 로그 기록 (원장성 로그)
        pointLogRepository.save(PointLog.from(memberId, amount, PointLogType.SAVING_APPROVAL.getCode(), pointSavingApprovalReq.getLogAt()));

        // 회원 잔액 증가
        member.addPointBalance(amount);
        memberRepository.save(member);

        // 포인트 지갑 생성 (만료일 단위 관리)
        PointWallet pointWallet = PointWallet.from(memberId, pointSavingApprovalReq);
        pointWalletRepository.save(pointWallet);

        PointResp pointResp = new PointResp();
        pointResp.setMemberId(memberId);
        pointResp.setAmount(amount);
        return pointResp;
    }

    /**
     * 포인트 적립 취소
     * <p>
     * - 특정 wallet 단위 취소
     * - 이미 사용된 포인트는 취소 불가
     */
    @Transactional
    public PointResp savingCancel(PointSavingCancelReq pointSavingCancelReq) {
        Long memberId = pointSavingCancelReq.getMemberId();
        Long amount = pointSavingCancelReq.getAmount();
        Long walletId = pointSavingCancelReq.getWalletId();

        PointLog pointLog = new PointLog();

        //거래구분코드
        pointLog.setLogType(PointLogType.SAVING_CANCEL.getCode());

        // 회원 조회 + 락
        Member member = getMember(memberId);

        // 잔액 부족 여부 확인
        validatePointBalance(member, amount);

        // 취소 로그 기록
        pointLogRepository.save(PointLog.from(memberId, amount, PointLogType.SAVING_CANCEL.getCode(), pointSavingCancelReq.getLogAt()));

        // 회원 잔액 차감
        member.subsPointBalance(amount);
        memberRepository.save(member);

        // 취소 대상 wallet 조회
        PointWallet cancelWallet = getCancelWallet(memberId, walletId);

        // 헤딩 wallet 비활성화 구분코드 취소로 변경
        cancelWallet.setWalletStatus(Val.CANCEL);
        pointWalletRepository.save(cancelWallet);

        return new PointResp(memberId, amount);
    }

    /**
     * 포인트 사용 승인
     * <p>
     * - 주문번호 생성
     * - 만료 임박 포인트 우선 사용
     */
    @Transactional
    public PointUseApprovalResp useApproval(PointUseApprovalReq pointUseApprovalReq) {
        Long memberId = pointUseApprovalReq.getMemberId();
        Long amount = pointUseApprovalReq.getAmount();

        // 회원 조회 + 잔액 검증
        Member member = getMember(memberId);
        validatePointBalance(member, amount);

        // 주문번호 생성
        String orderNo = orderNoGenerator.generateOrderNo();

        // 사용승인 로그 생성
        PointLog pointLog = PointLog.from(memberId, amount, PointLogType.USE_APPROVAL.getCode(), pointUseApprovalReq.getLogAt());
        pointLog.setOrderNo(orderNo);

        // 포인트 사용승인 처리 (wallet 차감 로직)
        usePoint(pointLog);
        pointLogRepository.save(pointLog);

        // 회원 잔액 차감
        member.subsPointBalance(amount);
        memberRepository.save(member);

        return new PointUseApprovalResp(memberId, orderNo, amount);
    }

    /**
     * 포인트 사용 취소
     * <p>
     * - 주문 기준 취소
     * - 만료된 포인트는 신규 wallet으로 재적립
     */
    @Transactional
    public PointResp useCancel(PointUseCancelReq pointUseCancelReq) {

        Long memberId = pointUseCancelReq.getMemberId();
        String orderNo = pointUseCancelReq.getOrderNo();
        Long cancelAmount = pointUseCancelReq.getAmount();

        // 회원 조회 + 잔액 검증
        Member member = getMember(memberId);

        // 해당 주문번호에 대한 사용 로그 조회
        PointLog useLogs = pointLogRepository.findUseLogsByOrderNoForUpdate(orderNo, PointLogType.USE_APPROVAL.getCode());

        if (useLogs == null) {
            throw new ServiceException("MP006");
        } else if (useLogs.getAmount() < cancelAmount) {
            throw new ServiceException("MP013");
        }

        // 실제 wallet 취소 처리
        useCancel(useLogs, cancelAmount);

        // 취소 로그 기록
        PointLog cancelLog = new PointLog();
        cancelLog.setMemberId(memberId);
        cancelLog.setOrderNo(orderNo);
        cancelLog.setLogType(PointLogType.USE_CANCEL.getCode());
        cancelLog.setAmount(cancelAmount);
        cancelLog.setLogAt(pointUseCancelReq.getLogAt());
        cancelLog.setCreatedAt(DateUtil.getLocalDateTimeWithNano());

        pointLogRepository.save(cancelLog);

        // 회원 잔액 복원
        member.setPointBalance(member.getPointBalance() + cancelAmount);
        memberRepository.save(member);

        PointResp pointResp = new PointResp();
        pointResp.setMemberId(memberId);
        pointResp.setAmount(member.getPointBalance());

        return new PointResp(memberId, cancelAmount);
    }

    /**
     * 회원 조회 + Row Lock
     * <p>
     * - 포인트 증감은 동시성 이슈에 민감하므로
     * 반드시 SELECT FOR UPDATE 로 회원을 조회
     */
    private Member getMember(Long memberId) {
        Member member = memberRepository.findByMemberIdForUpdate(memberId);
        if (member == null) throw new ServiceException(ProcessCode.HB001.getProcCd());
        return member;
    }

    /**
     * 적립 취소 대상 wallet 조회
     * <p>
     * 정책:
     * - 존재하지 않는 wallet 취소 불가
     * - 이미 사용된 wallet은 적립 취소 불가
     * - 활성 상태(wallet_status = '00')만 취소 가능
     */
    private PointWallet getCancelWallet(Long memberId, Long walletId) {
        PointWallet cancelWallet = pointWalletRepository.findByMemberIdAndWalletId(memberId, walletId);
        if (cancelWallet == null) {
            throw new ServiceException("MP006");
        } else if (cancelWallet.getUsedAmount() > 0) {
            throw new ServiceException("MP008");
        } else if (!cancelWallet.getWalletStatus().equals(Val.NORMAL)) {
            throw new ServiceException("MP009");
        }
        return cancelWallet;
    }

    /**
     * 회원 잔액 검증
     * <p>
     * - 사용 / 취소 공통으로 사용
     * - 잔액 부족 시 즉시 실패 처리
     */
    private void validatePointBalance(Member member, Long amount) {
        if (member.getPointBalance() < amount) {
            throw new ServiceException("MP010");
        }
    }

    /**
     * 적립 금액 검증
     * <p>
     * - 정책 테이블 기준 최소/최대 적립 금액 확인
     */
    private void validateSavingAmount(long amount) {

        //null값 체크하기
        PointPolicy minPolicy = pointPolicyRepository.findByPolicyKey(PointPolicyKey.POINT_SAVING_MIN.name());
        PointPolicy maxPolicy = pointPolicyRepository.findByPolicyKey(PointPolicyKey.POINT_SAVING_MAX.name());

        long min = minPolicy.getPolicyValue();
        long max = maxPolicy.getPolicyValue();

        if (amount < min ) {
            throw new ServiceException("MP002"); // 1원 이상 충전 가능
        }else if (amount > max) {
            throw new ServiceException("MP003"); // 적립금액 범위 초과
        }
    }

    /**
     * 회원 보유 한도 검증
     * <p>
     * - 적립 후 총 보유 포인트가
     * 정책 최대치를 초과하는지 확인
     */
    private void validateBalanceLimit(long currentBalance, long earnAmount) {
        PointPolicy maxBalancePolicy = pointPolicyRepository.findByPolicyKey(PointPolicyKey.POINT_BALANCE_MAX.name());
        long maxBalance = maxBalancePolicy.getPolicyValue();

        if (currentBalance + earnAmount > maxBalance) {
            throw new ServiceException("MP003"); // 보유한도 초과
        }
    }

    /**
     * 포인트 만료일 검증
     * <p>
     * 정책:
     * - 최소 1일 이상
     * - 최대 5년 미만
     */
    private LocalDate validateExpireDate(LocalDate expireDate) {
        LocalDate today = LocalDate.now();

        if (expireDate.isBefore(today.plusDays(1))) {
            throw new ServiceException("MP004");
        }
        if (!expireDate.isBefore(today.plusYears(5))) {
            throw new ServiceException("MP005");
        }
        return expireDate;
    }

    /**
     * 포인트 사용 처리 로직
     * <p>
     * 정책:
     * - 만료 임박 포인트 우선 사용 (FIFO)
     * - 실제 사용 가능 금액 = issued - used - expired
     * - 여러 wallet에 걸쳐 분할 차감 가능
     */
    public void usePoint(PointLog pointLog) {

        Long remainUseAmount = pointLog.getAmount(); // 남은 사용 금액

        // 사용 가능한 wallet 목록 조회 (만료일 오름차순)
        List<PointWallet> usablePointList = pointWalletRepository.findUsableWallets(pointLog.getMemberId());

        for (PointWallet pointWallet : usablePointList) {

            if (remainUseAmount <= 0) break;

            Long issuedAmount = pointWallet.getIssuedAmount();
            Long usedAmount = pointWallet.getUsedAmount();

            // 실제 사용 가능한 금액
            Long usableAmount = issuedAmount - usedAmount;

            if (usableAmount <= 0) continue;

            // 이번 wallet에서 사용할 금액
            Long useTarget = Math.min(usableAmount, remainUseAmount);

            // 사용 금액 누적
            pointWallet.setUsedAmount(pointWallet.getUsedAmount() + useTarget);
            pointWalletRepository.save(pointWallet);

            remainUseAmount -= useTarget;
        }

        // 모든 wallet을 사용해도 부족한 경우
        if (remainUseAmount > 0) {
            throw new ServiceException(ProcessCode.MP010.getProcCd());
        }

        // 주문 단위 사용 상세 로그 기록
        PointUseDetail pointUseDetail = new PointUseDetail();
        pointUseDetail.setOrderNo(pointLog.getOrderNo());
        pointUseDetail.setUsedAmount(pointLog.getAmount());
        pointUseDetail.setCreatedAt(DateUtil.getLocalDateTimeWithNano());

        pointUseDetailRepository.save(pointUseDetail);
    }

    /**
     * 포인트 사용 취소 처리
     * <p>
     * 정책:
     * - 취소 가능 금액은 "usedAmount 기준"
     * - 만료 여부는 취소 가능 여부가 아닌 "복구 방식"의 차이
     * - 만료된 포인트는 신규 wallet으로 재적립
     * - 사용 순서 역순(LIFO)으로 취소
     */

    @Transactional
    public void useCancel(PointLog useLogs, Long cancelAmount) {

        long remainCancelAmount = cancelAmount;
        long memberId = useLogs.getMemberId();
        long totalUsedAmount = useLogs.getAmount();

        // 1. 사전 검증
        long canceledAmount =
                pointLogRepository.getCanceledAmount(
                        useLogs.getOrderNo(),
                        PointLogType.USE_CANCEL.getCode()
                );

        long cancelableAmount = totalUsedAmount - canceledAmount;

        if (canceledAmount > cancelableAmount) {
            throw new ServiceException(ProcessCode.MP013.getProcCd());
        }

        // 2. 사용 역순 wallet 조회 (LIFO)
        List<PointWallet> cancelTargetList =
                pointWalletRepository.findCancelWallets(memberId);

        for (PointWallet wallet : cancelTargetList) {

            if (remainCancelAmount <= 0) break;

            long usedAmount = wallet.getUsedAmount();
            if (usedAmount <= 0) continue;

            // 이번 wallet에서 실제 취소할 금액
            long cancelTarget =
                    Math.min(usedAmount, remainCancelAmount);

            // 만료 wallet → 신규 적립
            if (Val.EXPIRED.equals(wallet.getWalletStatus())) {

                PointWallet newWallet = PointWallet.builder()
                        .memberId(memberId)
                        .issuedAmount(cancelTarget)
                        .usedAmount(0L)
                        .walletStatus(Val.NORMAL)
                        .expireDate(LocalDate.now().plusYears(1))
                        .sourceType(WalletSourceType.RESAVING)
                        .createdAt(LocalDateTime.now())
                        .build();

                pointWalletRepository.save(newWallet);

            }
            // 정상 wallet → 기존 wallet 복원
            else {
                wallet.setUsedAmount(usedAmount - cancelTarget);
                pointWalletRepository.save(wallet);
            }

            remainCancelAmount -= cancelTarget;
        }
    }
}
