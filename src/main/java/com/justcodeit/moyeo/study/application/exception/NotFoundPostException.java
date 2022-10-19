package com.justcodeit.moyeo.study.application.exception;

import com.justcodeit.moyeo.study.model.type.ErrorCode;
import com.justcodeit.moyeo.study.model.type.MoyeoException;

public class NotFoundPostException extends MoyeoException {

  private static final ErrorCode errorCode = ErrorCode.NOT_FOUND_POST;

  public NotFoundPostException() {
    super(errorCode);
  }
}
