package org.hana.wooahhanaapi.domain.community.exception;

import lombok.Getter;
import org.hana.wooahhanaapi.utils.exception.CustomExceptionData;
import org.hana.wooahhanaapi.utils.exception.GlobalException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class AutoDepositNotFoundException extends GlobalException {

    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    private final String exceptionName = "AutoDeposit Not Found Exception";

    public AutoDepositNotFoundException(String message) {
        super(message);
        CustomExceptionData.create(httpStatus, exceptionName);
    }

    @Override
    public LocalDateTime getTimeStamp() {
        return customExceptionData.getTimestamp();
    }
}
