package com.justcodeit.moyeo.study.model.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserToken {

  private String email;
  private String role;
  private String userId;
  private String nickname;
}
