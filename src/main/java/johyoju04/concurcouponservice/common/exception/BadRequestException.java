package johyoju04.concurcouponservice.common.exception;

public class BadRequestException extends ConcurCouponException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
