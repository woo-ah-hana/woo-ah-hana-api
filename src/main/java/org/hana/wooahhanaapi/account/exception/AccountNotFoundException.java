package org.hana.wooahhanaapi.account.exception;

import lombok.Getter;
import org.hana.wooahhanaapi.utils.exception.CustomExceptionData;
import org.hana.wooahhanaapi.utils.exception.GlobalException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class AccountNotFoundException extends GlobalException {
    public AccountNotFoundException(String message) {
        super(message);
        CustomExceptionData.create(httpStatus,exceptionName);
    }

    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    private final String exceptionName = "Account Not Found Exception";

    @Override
    public LocalDateTime getTimeStamp() {
        return customExceptionData.getTimestamp();
    }
}
