package com.justcodeit.moyeo.study.model.skill;

import com.justcodeit.moyeo.study.model.type.SkillCategory;
import com.justcodeit.moyeo.study.persistence.Skill;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StackInfoDto {

  private final SkillCategory category;
  private final String skillCode;
  private final String name;
  private final String imageUrl;

  public static StackInfoDto fromEntity(Skill s) {
    return new StackInfoDto(s.getCategory(), s.getSkillCode(), s.getName(), s.getImageUrl());
  }

}
