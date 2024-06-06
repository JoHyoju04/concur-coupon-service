package johyoju04.concurcouponservice.common.exception;

public enum ErrorCode {
    //400: BAD_REQUEST
    INVALID_REQUEST_ARGUMENT("잘못된 요청입니다."),
    COUPON_OVER_AMOUNT("발급 가능한 쿠폰 수량을 초과했습니다."),
    COUPON_OVER_AMOUNT_PER_MEMBER("한사람당 발급 가능한 쿠폰 수량을 초과했습니다."),
    NOT_ISSUED_TIME("쿠폰 발급 가능한 시간이 아닙니다."),

    //401 UNAUTHORIZED
    EXPIRED_AUTH_TOKEN("만료된 로그인 토큰입니다."),
    INVALID_AUTH_TOKEN("올바르지 않은 로그인 토큰입니다."),
    NOT_BEARER_TOKEN_TYPE("Bearer 타입의 토큰이 아닙니다."),
    NEED_AUTH_TOKEN("로그인이 필요한 서비스입니다."),
    INCORRECT_PASSWORD_OR_ACCOUNT("비밀번호가 틀렸거나, 해당 계정이 없습니다."),
    DUPLICATE_ACCOUNT_USERNAME("해당 계정이 존재합니다."),
    INVALID_REFRESH_TOKEN("존재하지 않은 리프래쉬 토큰으로 재발급 요청을 했습니다."),
    EXPIRED_REFRESH_TOKEN("만료된 리프래쉬 토큰입니다."),

    //403 FORBIDDEN
    NOT_ENOUGH_PERMISSION("해당 권한이 없습니다."),

    //404 NOT_FOUND
    COUPON_GROUP_NOT_FOUND("존재하지 않은 그룹 쿠폰입니다."),
    MEMBER_NOT_FOUND("존재하지 않는 멤버입니다."),
    COUPON_NOT_FOUND("존재하지 않은 쿠폰입니다."),

    //500 INTERNAL_SERVER_ERROR
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
