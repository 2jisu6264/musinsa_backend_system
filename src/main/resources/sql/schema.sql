CREATE TABLE member
(
    member_id          BIGINT AUTO_INCREMENT COMMENT '회원 ID',
    member_name        VARCHAR(50) COMMENT '회원명',
    point_balance      BIGINT DEFAULT 0 COMMENT '보유 포인트',
    created_at         DATETIME(0) NOT NULL COMMENT '등록일시',
    PRIMARY KEY (member_id)
);
CREATE TABLE order_sequence_log
(
    order_date   CHAR(15)     NOT NULL,
    seq     INT         NOT NULL,
    created_at   TIMESTAMP   NOT NULL,
    PRIMARY KEY (order_date)
);
CREATE TABLE point_log
(
    log_id          BIGINT AUTO_INCREMENT COMMENT '거래 ID',
    member_id       BIGINT      NOT NULL COMMENT '회원 ID',
    log_type        CHAR(2)     NOT NULL COMMENT '거래 구분 코드',
    log_at          CHAR(20)     NOT NULL COMMENT '거래일시',
    order_no        VARCHAR(20) COMMENT '주문 번호',
    amount   BIGINT      NOT NULL COMMENT '요청 금액',
    created_at      DATETIME(0) NOT NULL COMMENT '등록일시',
    PRIMARY KEY (log_id)
);
CREATE TABLE point_use_detail
(
    use_id      BIGINT AUTO_INCREMENT COMMENT '거래 ID',
    order_no    VARCHAR(20) NOT NULL COMMENT '주문 번호',
    used_amount BIGINT      NOT NULL COMMENT '사용 금액',
    created_at  DATETIME(0) NOT NULL,
    PRIMARY KEY (use_id)
);
CREATE TABLE point_wallet
(
    wallet_id      BIGINT AUTO_INCREMENT COMMENT '포인트 지갑 ID',
    member_id      BIGINT  NOT NULL COMMENT '회원 ID',
    issued_amount  BIGINT  NOT NULL COMMENT '지급 금액',
    used_amount    BIGINT DEFAULT 0 COMMENT '사용 금액',
    expired_amount BIGINT DEFAULT 0 COMMENT '만료 금액',
    wallet_status  CHAR(2) DEFAULT 00 NOT NULL COMMENT '지갑 상태 (00:보관, 01:취소,02:만료)',
    expire_date    DATETIME(0) NOT NULL COMMENT '만료 일자',
    source_type CHAR(2) NOT NULL COMMENT '지급 출처',
    created_at     DATETIME(0) NOT NULL COMMENT '등록일시',
    PRIMARY KEY (wallet_id)
);

CREATE TABLE point_policy
(
    policy_key   VARCHAR(50) PRIMARY KEY COMMENT '정책 키',
    policy_value BIGINT NOT NULL COMMENT '정책 값',
    description  VARCHAR(200) COMMENT '설명'
);
