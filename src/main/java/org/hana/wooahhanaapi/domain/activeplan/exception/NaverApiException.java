package org.hana.wooahhanaapi.domain.activeplan.exception;

import lombok.Getter;
import org.hana.wooahhanaapi.utils.exception.CustomExceptionData;
import org.hana.wooahhanaapi.utils.exception.GlobalException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class NaverApiException extends GlobalException {
    private final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    private final String exceptionName = "Naver Api Exception";

    public NaverApiException(String message) {
        super(message);
        this.customExceptionData = CustomExceptionData.create(httpStatus, exceptionName);
    }

    @Override
    public LocalDateTime getTimeStamp() {
        return customExceptionData.getTimestamp();
    }
}
