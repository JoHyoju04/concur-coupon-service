package johyoju04.concurcouponservice.common.exception.dto;

import johyoju04.concurcouponservice.common.exception.ConcurCouponException;
import johyoju04.concurcouponservice.common.exception.ErrorCode;

public record ErrorResponse(
        ErrorCode errorCode,
        String message
) {

    public static ErrorResponse from(ConcurCouponException concurCouponException) {
        return ErrorResponse.from(concurCouponException.getErrorCode());
    }

    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode, errorCode.getMessage());
    }
}
