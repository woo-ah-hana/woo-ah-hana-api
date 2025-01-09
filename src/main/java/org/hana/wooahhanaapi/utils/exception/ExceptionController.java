package org.hana.wooahhanaapi.utils.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.hana.wooahhanaapi.domain.member.exception.DuplicateUsernameException;
import org.hana.wooahhanaapi.domain.member.exception.UserNotFoundException;
import org.hana.wooahhanaapi.domain.plan.exception.InvalidPlanDataException;

import org.hana.wooahhanaapi.domain.plan.exception.LogicalPlanDataException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        String em = e.getMessage();
        return new ResponseEntity<>(em, e.getHttpStatus());
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<String> handleDuplicateUsernameException(DuplicateUsernameException e) {
        String em = e.getMessage();
        return new ResponseEntity<>(em, e.getHttpStatus());
    }

    @ExceptionHandler(InvalidPlanDataException.class)
    public ResponseEntity<String> handleInvalidPlanDataException(InvalidPlanDataException e) {
        String em = e.getMessage();
        return new ResponseEntity<>(em, e.getHttpStatus());
    }

    @ExceptionHandler(LogicalPlanDataException.class)
    public ResponseEntity<String> handleLogicalPlanDataException(LogicalPlanDataException e) {
        String em = e.getMessage();
        return new ResponseEntity<>(em, e.getHttpStatus());
    }

//    @ExceptionHandler(GlobalException.class)
//    public ResponseEntity<String> handleRuntimeException(GlobalException e) {
//        String em = e.getMessage();
//        return new ResponseEntity<>(em, e.getHttpStatus());
//    }
}
