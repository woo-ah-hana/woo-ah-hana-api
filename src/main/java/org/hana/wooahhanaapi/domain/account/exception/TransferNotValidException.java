package org.hana.wooahhanaapi.domain.account.exception;

import lombok.Getter;
import org.hana.wooahhanaapi.utils.exception.CustomExceptionData;
import org.hana.wooahhanaapi.utils.exception.GlobalException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class TransferNotValidException extends GlobalException {

  private final HttpStatus httpStatus = HttpStatus.FORBIDDEN;
  private final String exceptionName = "Transfer Not Valid Exception";

  public TransferNotValidException(String message) {
    super(message);
    CustomExceptionData.create(httpStatus,exceptionName);
  }

  @Override
  public LocalDateTime getTimeStamp() {
    return customExceptionData.getTimestamp();
  }
}
