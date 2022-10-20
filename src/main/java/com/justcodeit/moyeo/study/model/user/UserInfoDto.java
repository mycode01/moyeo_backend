package com.justcodeit.moyeo.study.model.user;

import com.justcodeit.moyeo.study.persistence.User;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoDto {

  private final String userId;
  private final String nickname;
  private final String email;
  private final String introduce;
  private final String picture;
  private final Set<String> skills;

  public static UserInfoDto fromEntity(User u) {
    return new UserInfoDto(u.getUserId(), u.getNickname(), u.getEmail(),
        u.getIntroduce(), u.getPicture(), u.getSkillTags());
  }
}
