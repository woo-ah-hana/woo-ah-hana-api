package org.hana.wooahhanaapi.domain.plan.exception;

import lombok.Getter;
import org.hana.wooahhanaapi.utils.exception.CustomExceptionData;
import org.hana.wooahhanaapi.utils.exception.GlobalException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

// 필수 데이터 누락 예외
@Getter
public class InvalidPlanDataException extends GlobalException {
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    private final String exceptionName = "Invalid Plan Data Exception";

    public InvalidPlanDataException(String message) {
        super(message);
        CustomExceptionData.create(httpStatus,exceptionName);
    }
    @Override
    public LocalDateTime getTimeStamp() {
        return customExceptionData.getTimestamp();
    }

}
