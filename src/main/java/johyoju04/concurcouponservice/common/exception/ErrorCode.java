package johyoju04.concurcouponservice.common.exception;

public enum ErrorCode {
    // 400
    INVALID_REQUEST_ARGUMENT("잘못된 요청입니다."),
    COUPON_OVER_AMOUNT("발급 가능한 쿠폰 수량을 초과했습니다."),
    COUPON_OVER_AMOUNT_PER_MEMBER("한사람당 발급 가능한 쿠폰 수량을 초과했습니다."),
    NOT_ISSUED_TIME("쿠폰 발급 가능한 시간이 아닙니다."),

    // 404
    COUPON_GROUP_NOT_FOUND("존재하지 않은 그룹 쿠폰입니다."),
    MEMBER_NOT_FOUND("존재하지 않는 멤버입니다."),
    COUPON_NOT_FOUND("존재하지 않은 쿠폰입니다."),

    // 500
    INTERNAL_SERVER_ERROR("서버 내부에 문제가 발생했습니다."),
    FOR_TEST_ERROR("테스트용 에러입니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
