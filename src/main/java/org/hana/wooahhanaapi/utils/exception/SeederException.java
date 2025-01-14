package org.hana.wooahhanaapi.utils.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class SeederException extends GlobalException {
  private final HttpStatus httpStatus = HttpStatus.CONFLICT;

  private final String exceptionName = "Seeder Exception";

  public SeederException(String message) {
    super(message);
    CustomExceptionData.create(httpStatus, exceptionName);
  }

  @Override
  public LocalDateTime getTimeStamp() {
    return customExceptionData.getTimestamp();
  }
}
