package com.justcodeit.moyeo.study;


public class BaseResponse {

  int code;

  public BaseResponse(int code) {
    this.code = code;
  }

  public BaseResponse() {
  }

  public int getCode() {
    return code;
  }
}
