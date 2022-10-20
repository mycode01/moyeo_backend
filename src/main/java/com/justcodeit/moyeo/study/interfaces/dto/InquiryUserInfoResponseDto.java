package com.justcodeit.moyeo.study.interfaces.dto;

import com.justcodeit.moyeo.study.model.user.UserInfoDto;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InquiryUserInfoResponseDto {

  private final String userId;
  private final String nickname;
  private final String introduce;
  private final String picture;
  private final Set<String> skills;

  public static InquiryUserInfoResponseDto fromUserInfo(UserInfoDto d) {
    return new InquiryUserInfoResponseDto(d.getUserId(), d.getNickname(),
        d.getIntroduce(), d.getPicture(), d.getSkills());
  }
}
