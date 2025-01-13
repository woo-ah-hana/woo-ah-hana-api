package org.hana.wooahhanaapi.domain.community.exception;

import lombok.Getter;
import org.hana.wooahhanaapi.utils.exception.CustomExceptionData;
import org.hana.wooahhanaapi.utils.exception.GlobalException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class NotAMemberException extends GlobalException {

    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    private final String exceptionName = "Member Not A Member Of Community Exception";

    public NotAMemberException(String message) {
        super(message);
        CustomExceptionData.create(httpStatus, exceptionName);
    }
    @Override
    public LocalDateTime getTimeStamp() {
        return customExceptionData.getTimestamp();
    }


}
