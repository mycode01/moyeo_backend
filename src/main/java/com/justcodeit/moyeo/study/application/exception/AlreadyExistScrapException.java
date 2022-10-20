package com.justcodeit.moyeo.study.application.exception;

import com.justcodeit.moyeo.study.model.type.ErrorCode;
import com.justcodeit.moyeo.study.model.type.MoyeoException;

public class AlreadyExistScrapException extends MoyeoException {

  private final static ErrorCode errorCode = ErrorCode.DUPLICATED_OPERATION;

  public AlreadyExistScrapException() {
    super(errorCode);
  }
}
