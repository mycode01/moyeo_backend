package com.justcodeit.moyeo.study.model.type;

public enum ErrorCode {

  SYSTEM_EXCEPTION(500, "S000", "System Exception"),
  NOT_FOUND_HANDLER(404, "S404", "404 NOT FOUND"),

  INVALID_INPUT_VALUE(400, "B001", "Invalid Input Value"),
  METHOD_NOT_ALLOWED(405, "B002", "Invalid Input Value"),
  HANDLE_ACCESS_DENIED(403, "B006", "Access is Denied"),
  INVALID_OPERATION(400, "B010", "Invalid Operation"),
  DUPLICATED_OPERATION(409, "B011", "Duplicated Operation"),
  INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
  INVALID_TYPE_VALUE(400, "C005", "Invalid Type Value"),

  NOT_FOUND_USER(404, "U001", "No such User"),
  NOT_FOUND_POST(404, "P001", "No such Post"),

  JWT_DECODE_FAILURE(500, "J001", "JWT cannot be decoded"),
  JWT_ENCODE_FAILURE(500, "J002", "DTO encode failure"),

  FILE_FETCH_FAILURE(500, "S001", "File fetch failure (from storage)"),

  ;
  private final String code;
  private final String message;
  private int status;

  ErrorCode(final int status, final String code, final String message) {
    this.status = status;
    this.message = message;
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public int getStatus() {
    return status;
  }
}