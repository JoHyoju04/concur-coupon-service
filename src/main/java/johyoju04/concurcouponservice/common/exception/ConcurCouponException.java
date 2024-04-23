package johyoju04.concurcouponservice.common.exception;

import org.springframework.core.NestedRuntimeException;

public abstract class ConcurCouponException extends NestedRuntimeException {
    private final ErrorCode errorCode;

    protected ConcurCouponException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    protected ConcurCouponException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    protected ConcurCouponException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
