package org.hana.wooahhanaapi.domain.plan.exception;

import lombok.Getter;
import org.hana.wooahhanaapi.utils.exception.CustomExceptionData;
import org.hana.wooahhanaapi.utils.exception.GlobalException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

// 논리적 오류 예외
@Getter
public class LogicalPlanDataException extends GlobalException {
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    private final String exceptionName = "Start_date is bigger than end_date";

    public LogicalPlanDataException(String message) {
        super(message);
        CustomExceptionData.create(httpStatus,exceptionName);
    }

    @Override
    public LocalDateTime getTimeStamp() {
        return customExceptionData.getTimestamp();
    }
}
