package org.hana.wooahhanaapi.domain.activeplan.exception;

import lombok.Getter;
import org.hana.wooahhanaapi.utils.exception.CustomExceptionData;
import org.hana.wooahhanaapi.utils.exception.GlobalException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class InvalidSearchQueryException extends GlobalException {

    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private final String exceptionName = "Invalid Search Query Exception";

    public InvalidSearchQueryException(String message) {
        super(message);
        this.customExceptionData = CustomExceptionData.create(httpStatus, exceptionName);
    }

    @Override
    public LocalDateTime getTimeStamp() {
        return customExceptionData.getTimestamp();
    }

}
