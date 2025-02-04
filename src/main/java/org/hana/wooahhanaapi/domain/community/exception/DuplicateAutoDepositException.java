package org.hana.wooahhanaapi.domain.community.exception;

import lombok.Getter;
import org.hana.wooahhanaapi.utils.exception.CustomExceptionData;
import org.hana.wooahhanaapi.utils.exception.GlobalException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class DuplicateAutoDepositException extends GlobalException {

    public DuplicateAutoDepositException(String message) {
        super(message);
        CustomExceptionData.create(httpStatus, exceptionName);
    }
    private final HttpStatus httpStatus = HttpStatus.CONFLICT;

    private final String exceptionName = "Duplicate AutoDepsit Exception";

    @Override
    public LocalDateTime getTimeStamp() {
        return customExceptionData.getTimestamp();
    }
}
