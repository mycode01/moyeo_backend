package com.justcodeit.moyeo.study.model.post;

import com.justcodeit.moyeo.study.common.PostIdGenerator;
import com.justcodeit.moyeo.study.model.type.GatherType;
import com.justcodeit.moyeo.study.model.type.GroupType;
import com.justcodeit.moyeo.study.model.type.PostState;
import com.justcodeit.moyeo.study.persistence.Post;
import com.justcodeit.moyeo.study.persistence.PostSkill;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReqUpdatePostDto {

  private final String postId;
  private final String userId;
  private final String title;
  private final String describe;
  private final String contact;
  private final GroupType groupType;
  private final GatherType gatherType;
  private final List<String> skillCode;
  private final Map<String, Integer> members;

  public Set<PostSkill> skills() {
    return skillCode.stream().map(PostSkill::new).collect(Collectors.toSet());
  }
}
