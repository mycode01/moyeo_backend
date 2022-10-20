package com.justcodeit.moyeo.study.interfaces.dto;

import com.justcodeit.moyeo.study.model.user.UserInfoDto;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InquiryUserInfoResponse {

  private final String userId;
  private final String nickname;
  private final String introduce;
  private final String picture;
  private final Set<String> skills;

  public static InquiryUserInfoResponse fromUserInfo(UserInfoDto d) {
    return new InquiryUserInfoResponse(d.getUserId(), d.getNickname(),
        d.getIntroduce(), d.getPicture(), d.getSkills());
  }
}
