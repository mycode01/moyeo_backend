package com.justcodeit.moyeo.study.model.type;

public class UndecidedOperationException extends MoyeoException {

  private final static ErrorCode errorCode = ErrorCode.INVALID_OPERATION;

  public UndecidedOperationException() {
    super(errorCode);
  }
}
