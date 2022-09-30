package com.justcodeit.moyeo.study.interfaces.dto;


public class BaseResponse {

  int code;
  public BaseResponse(int code) {
    this.code = code;
  }

  private BaseResponse(){}

  public int getCode() {
    return code;
  }
}
