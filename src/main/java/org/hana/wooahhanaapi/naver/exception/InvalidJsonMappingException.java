package org.hana.wooahhanaapi.naver.exception;

import lombok.Getter;
import org.hana.wooahhanaapi.utils.exception.CustomExceptionData;
import org.hana.wooahhanaapi.utils.exception.GlobalException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class InvalidJsonMappingException extends GlobalException {

    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private final String exceptionName = "Invalid Json Mapping Exception";

    public InvalidJsonMappingException(String message) {
        super(message);
        this.customExceptionData = CustomExceptionData.create(httpStatus, exceptionName);
    }

    @Override
    public LocalDateTime getTimeStamp() {
        return customExceptionData.getTimestamp();
    }

}
