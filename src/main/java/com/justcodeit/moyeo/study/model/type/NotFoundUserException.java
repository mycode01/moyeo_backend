package com.justcodeit.moyeo.study.model.type;

public class NotFoundUserException extends MoyeoException {

  private final static ErrorCode errorCode = ErrorCode.NOT_FOUND_USER;

  public NotFoundUserException() {
    super(errorCode);
  }
}
