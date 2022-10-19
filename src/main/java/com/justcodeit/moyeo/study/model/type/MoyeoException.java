package com.justcodeit.moyeo.study.model.type;

public class MoyeoException extends RuntimeException {
  private ErrorCode errorCode;

  public MoyeoException(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public MoyeoException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }
}
