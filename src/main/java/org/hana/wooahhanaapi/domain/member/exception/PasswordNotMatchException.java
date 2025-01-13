package org.hana.wooahhanaapi.domain.member.exception;

import lombok.Getter;
import org.hana.wooahhanaapi.utils.exception.CustomExceptionData;
import org.hana.wooahhanaapi.utils.exception.GlobalException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class PasswordNotMatchException extends GlobalException {

    private final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    private final String exceptionName = "Password Not Match Exception";

    public PasswordNotMatchException(String message) {
        super(message);
        CustomExceptionData.create(httpStatus, exceptionName);
    }

    @Override
    public LocalDateTime getTimeStamp() {
        return customExceptionData.getTimestamp();
    }

}
