package org.hana.wooahhanaapi.domain.community.exception;

import lombok.Getter;
import org.hana.wooahhanaapi.utils.exception.CustomExceptionData;
import org.hana.wooahhanaapi.utils.exception.GlobalException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class NoAuthorityException extends GlobalException {
    public NoAuthorityException(String message) {
        super(message);
        CustomExceptionData.create(httpStatus, exceptionName);
    }

  private final HttpStatus httpStatus = HttpStatus.FORBIDDEN;

  private final String exceptionName = "Member Has No Authority Exception";

  @Override
  public LocalDateTime getTimeStamp() {
    return customExceptionData.getTimestamp();
  }
}
