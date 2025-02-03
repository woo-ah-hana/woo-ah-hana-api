package org.hana.wooahhanaapi.account.exception;

import lombok.Getter;
import org.hana.wooahhanaapi.utils.exception.CustomExceptionData;
import org.hana.wooahhanaapi.utils.exception.GlobalException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class IncorrectValidationCodeException extends GlobalException {

    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private final String exceptionName = "Incorrect Validation Code Exception";

    public IncorrectValidationCodeException(String message) {
        super(message);
        CustomExceptionData.create(httpStatus,exceptionName);
    }

    @Override
    public LocalDateTime getTimeStamp() {
        return customExceptionData.getTimestamp();
    }

}
