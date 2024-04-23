package johyoju04.concurcouponservice.common.exception;

public class NotFoundException extends ConcurCouponException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
