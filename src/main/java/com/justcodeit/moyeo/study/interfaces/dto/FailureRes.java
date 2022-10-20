package com.justcodeit.moyeo.study.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

@Data
@EqualsAndHashCode(callSuper = false)
public class FailureRes extends BaseResponse {

  private String errorCode;
  private String message;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private List<ValidationError> errors;

  public FailureRes(int statusCode, String errorCode, String message) {
    super(statusCode);
    this.errorCode = errorCode;
    this.message = message;
  }

  public FailureRes(List<ValidationError> errors, int statusCode, String errorCode, String message) {
    super(statusCode);
    this.errorCode = errorCode;
    this.message = message;
    this.errors = errors;
  }

  @Getter
  @RequiredArgsConstructor
  public static class ValidationError {

    private final String field;
    private final String message;

    public static ValidationError of(final FieldError fieldError) {
      return new ValidationError(fieldError.getField(), fieldError.getDefaultMessage());
    }
  }
}
