package com.justcodeit.moyeo.study.model.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserToken {

  String role;
  String username;
}
