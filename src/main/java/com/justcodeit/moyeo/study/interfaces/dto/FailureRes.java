package com.justcodeit.moyeo.study.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class FailureRes<T> extends BaseResponse {

  private String message;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private T data;

  public FailureRes(int code, String message) {
    super(code);
    this.message = message;
  }

  public FailureRes(int code, String message, T data) {
    super(code);
    this.message = message;
    this.data = data;
  }
}
