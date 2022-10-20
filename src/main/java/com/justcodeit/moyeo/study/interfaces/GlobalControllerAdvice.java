package com.justcodeit.moyeo.study.interfaces;

import com.justcodeit.moyeo.study.interfaces.dto.FailureRes;
import com.justcodeit.moyeo.study.interfaces.dto.FailureRes.ValidationError;
import com.justcodeit.moyeo.study.model.type.ErrorCode;
import com.justcodeit.moyeo.study.model.type.MoyeoException;
import com.justcodeit.moyeo.study.model.type.UndecidedOperationException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {


  @ExceptionHandler(MoyeoException.class)
  public ResponseEntity<Object> globalExceptionHandler(MoyeoException e) {
    return handleExceptionInternal(e);
  }

  @ExceptionHandler(UndecidedOperationException.class)
  public ResponseEntity<Object> undecidedExceptionHandler(UndecidedOperationException e) {
    return handleExceptionInternal(e);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Object> accessDeniedExceptionHandler(AccessDeniedException e) {
    ErrorCode errorCode = ErrorCode.HANDLE_ACCESS_DENIED;
    return handleExceptionInternal(errorCode);
  }

  @Override
  protected ResponseEntity<Object> handleBindException(
      BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

    return handleExceptionInternal(ex, ErrorCode.INVALID_INPUT_VALUE);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {

    return handleExceptionInternal(ex, ErrorCode.INVALID_INPUT_VALUE);
  }

  @ExceptionHandler(UnsupportedOperationException.class)
  public ResponseEntity<Object> unsupportedOperationHandler(UnsupportedOperationException e) {
    e.printStackTrace();
    return handleExceptionInternal(ErrorCode.INTERNAL_SERVER_ERROR); // 해당 에러 발생시 메소드 구현해야 함.
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleAllException(Exception e) {
    ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    e.printStackTrace();
    return handleExceptionInternal(errorCode);
  }

  private ResponseEntity<Object> handleExceptionInternal(MoyeoException e) {
    ErrorCode errorCode = e.getErrorCode();
    return ResponseEntity.status(errorCode.getStatus())
        .body(buildResponse(errorCode));
  }

  private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
    return ResponseEntity.status(errorCode.getStatus())
        .body(buildResponse(errorCode));
  }

  private ResponseEntity<Object> handleExceptionInternal(BindException e, ErrorCode errorCode) {
    List<ValidationError> ve = e.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(ValidationError::of)
        .collect(Collectors.toList());
    return ResponseEntity.status(errorCode.getStatus())
        .body(buildResponse(errorCode, errorCode.getMessage(), ve));
  }

  private FailureRes buildResponse(ErrorCode errorCode) {
    return new FailureRes(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage());
  }

  private FailureRes buildResponse(ErrorCode errorCode, String message) {
    return new FailureRes(null, errorCode.getStatus(), errorCode.getCode(), message);
  }

  private FailureRes buildResponse(ErrorCode errorCode, String message, List<ValidationError> ve) {
    return new FailureRes(ve, errorCode.getStatus(), errorCode.getCode(), message);
  }

}
