INSERT INTO point_policy VALUES
                             ('POINT_SAVING_MIN', 1, '1회 최소 적립 포인트'),
                             ('POINT_SAVING_MAX', 100000, '1회 최대 적립 포인트'),
                             ('POINT_BALANCE_MAX', 500000, '회원 보유 최대 무료 포인트');
INSERT INTO member (
    member_name,
    point_balance,
    created_at
) VALUES
      ('홍길동', 0, '20260108'),
      ('김철수', 10000,  '20260108');
INSERT INTO order_sequence_log (
    order_date,
    seq,
    created_at
) VALUES
      ('20250809', 0001,  CURRENT_TIMESTAMP),
      ('20250810', 0002,  CURRENT_TIMESTAMP);
INSERT INTO point_wallet
(
    member_id,
    issued_amount,
    used_amount,
    expired_amount,
    wallet_status,
    expire_date,
    source_type,
    created_at
)
VALUES
    (
        2,
        10000,
        0,
        0,
        '00',
        '2026-12-31 23:59:59',
        'MA',
        CURRENT_TIMESTAMP
    );

INSERT INTO point_log
(
    member_id,
    log_type,
    log_at,
    order_no,
    amount,
    created_at
)
VALUES
    (
        2,
        'SA',
        FORMATDATETIME(CURRENT_TIMESTAMP, 'yyyy-MM-dd''T''HH:mm:ss'),
        NULL,
        10000,
        CURRENT_TIMESTAMP
    );

COMMIT;