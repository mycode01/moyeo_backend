package com.justcodeit.moyeo.study.interfaces.dto;

import com.justcodeit.moyeo.study.model.user.UserInfoDto;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InquiryMyInfoResponseDto {

  private final String userId;
  private final String nickname;
  private final String email;
  private final String introduce;
  private final String picture;
  private final Set<String> skills;

  public static InquiryMyInfoResponseDto fromUserInfo(UserInfoDto d) {
    return new InquiryMyInfoResponseDto(d.getUserId(), d.getNickname(), d.getEmail(),
        d.getIntroduce(), d.getPicture(), d.getSkills());
  }
}
