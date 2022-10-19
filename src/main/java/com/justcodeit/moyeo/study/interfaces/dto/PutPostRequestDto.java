package com.justcodeit.moyeo.study.interfaces.dto;

import com.justcodeit.moyeo.study.model.post.ReqCreatePostDto;
import com.justcodeit.moyeo.study.model.type.GatherType;
import com.justcodeit.moyeo.study.model.type.GroupType;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PutPostRequestDto {

  private final String postId;
  private final String title;
  private final String describe;
  private final GroupType groupType;
  private final GatherType gatherType;
  private final String contact;
  private final List<String> skillCode;
  private final Map<String, Integer> members;

  public ReqCreatePostDto toModel(String userId) {
    return new ReqCreatePostDto(userId, this.title, this.describe, this.contact, this.groupType,
        this.gatherType, this.skillCode, this.members);
  }
}
