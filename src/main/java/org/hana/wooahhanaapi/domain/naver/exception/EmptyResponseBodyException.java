package org.hana.wooahhanaapi.domain.naver.exception;
import lombok.Getter;
import org.hana.wooahhanaapi.utils.exception.CustomExceptionData;
import org.hana.wooahhanaapi.utils.exception.GlobalException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class EmptyResponseBodyException extends GlobalException {
    private final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    private final String exceptionName = "Empty ResponseBody Exception";

    public EmptyResponseBodyException(String query) {
        super("응답 본문이 비어 있습니다. Query: " + query);
        this.customExceptionData = CustomExceptionData.create(httpStatus, exceptionName);
    }

    @Override
    public LocalDateTime getTimeStamp() {
        return customExceptionData.getTimestamp();
    }
}