package com.justcodeit.moyeo.study.application.exception;

import com.justcodeit.moyeo.study.model.type.ErrorCode;
import com.justcodeit.moyeo.study.model.type.MoyeoException;

public class NotFoundUserException extends MoyeoException {

  private final static ErrorCode errorCode = ErrorCode.NOT_FOUND_USER;

  public NotFoundUserException() {
    super(errorCode);
  }
}
