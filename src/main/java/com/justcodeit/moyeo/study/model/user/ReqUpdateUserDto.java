package com.justcodeit.moyeo.study.model.user;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReqUpdateUserDto {

  private final String nickname;
  private final String introduce;
  private final Set<String> skills;
}
