package com.justcodeit.moyeo.study.model.post;

import com.justcodeit.moyeo.study.model.type.GatherType;
import com.justcodeit.moyeo.study.model.type.GroupType;
import com.justcodeit.moyeo.study.model.type.PostState;
import com.justcodeit.moyeo.study.persistence.Post;
import com.justcodeit.moyeo.study.persistence.PostSkill;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResPostDetailDto {

  private final String postId;
  private final String ownerId;
  private final PostState postState;
  private final String title;
  private final String content;
  private final String contact;
  private final GroupType groupType;
  private final GatherType gatherType;
  private final Set<String> skillTags;
  private final Map<String, Integer> members;

  public static ResPostDetailDto fromEntity(Post p) {
    return new ResPostDetailDto(p.getPostId(), p.getOwnerId(), p.getState(), p.getTitle(),
        p.getContent(), p.getContact(), p.getGroupType(), p.getGatherType(),
        skill(p.getSkillTags()), p.getRecruitMember());
  }

  private static Set<String> skill(Set<PostSkill> ps) {
    return ps.stream().map(PostSkill::getSkillCode).collect(Collectors.toSet());
  }
}
