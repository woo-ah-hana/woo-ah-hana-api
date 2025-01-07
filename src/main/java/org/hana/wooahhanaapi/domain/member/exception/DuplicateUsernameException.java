package org.hana.wooahhanaapi.domain.member.exception;

import lombok.Getter;
import org.hana.wooahhanaapi.utils.exception.CustomExceptionData;
import org.hana.wooahhanaapi.utils.exception.GlobalException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class DuplicateUsernameException extends GlobalException {
    private final HttpStatus httpStatus = HttpStatus.CONFLICT;

    private final String exceptionName = "Duplicate Username Exception";

    public DuplicateUsernameException(String message) {
        super(message);
        CustomExceptionData.create(httpStatus, exceptionName);
    }

    @Override
    public LocalDateTime getTimeStamp() {
        return customExceptionData.getTimestamp();
    }
}
