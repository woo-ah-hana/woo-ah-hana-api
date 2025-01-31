package org.hana.wooahhanaapi.firebase.exception;

import lombok.Getter;
import org.hana.wooahhanaapi.utils.exception.CustomExceptionData;
import org.hana.wooahhanaapi.utils.exception.GlobalException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class FirebaseException extends GlobalException {
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private final String exceptionName = "Firebase Exception";

    public FirebaseException(String message) {
        super(message);
        this.customExceptionData = CustomExceptionData.create(httpStatus, exceptionName);
    }

    @Override
    public LocalDateTime getTimeStamp() {
        return customExceptionData.getTimestamp();
    }
}
