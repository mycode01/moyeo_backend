package com.justcodeit.moyeo.study.model.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
  USER("ROLE_USER", "일반사용자"),
  GUEST("GUEST_USER", "게스트");
  private final String key;
  private final String title;
}
