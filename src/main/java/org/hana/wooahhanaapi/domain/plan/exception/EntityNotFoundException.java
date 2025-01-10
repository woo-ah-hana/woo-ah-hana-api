package org.hana.wooahhanaapi.domain.plan.exception;

import lombok.Getter;
import org.hana.wooahhanaapi.utils.exception.CustomExceptionData;
import org.hana.wooahhanaapi.utils.exception.GlobalException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class EntityNotFoundException extends GlobalException {

    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    private final LocalDateTime timeStamp = LocalDateTime.now();
    private final String exceptionName = "Entity Not Found Exception";

    public EntityNotFoundException(String message) {
        super(message);
        this.customExceptionData = CustomExceptionData.create(httpStatus, exceptionName);
    }

    @Override
    public LocalDateTime getTimeStamp() {
        return this.timeStamp;
    }
}