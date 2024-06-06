package johyoju04.concurcouponservice.common.handler;

import jakarta.servlet.http.HttpServletRequest;
import johyoju04.concurcouponservice.common.exception.BadRequestException;
import johyoju04.concurcouponservice.common.exception.ConcurCouponException;
import johyoju04.concurcouponservice.common.exception.NotFoundException;
import johyoju04.concurcouponservice.common.exception.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger("ErrorLogger");
    private static final String LOG_FORMAT_INFO = "\n[🔵INFO] - ({} {})";
    private static final String LOG_FORMAT_WARN = "\n[🟠WARN] - ({} {})";
    private static final String LOG_FORMAT_ERROR = "\n[🔴ERROR] - ({} {})";

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handle(BadRequestException e, HttpServletRequest request) {
        logError(e,request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(NotFoundException e, HttpServletRequest request) {
        logError(e,request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.from(e));
    }

    private void logError(Exception e, HttpServletRequest request) {
        log.error(LOG_FORMAT_ERROR, request.getMethod(), request.getRequestURI(), e);
    }
}
