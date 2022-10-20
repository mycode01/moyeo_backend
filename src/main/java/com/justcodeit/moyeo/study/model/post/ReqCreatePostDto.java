package com.justcodeit.moyeo.study.model.post;

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
public class ReqCreatePostDto {

  private final String userId;
  private final String title;
  private final String describe;
  private final String contact;
  private final GroupType groupType;
  private final GatherType gatherType;
  private final List<String> skillCode;
  private final Map<String, Integer> members;

  public Post toEntity(String postId, LocalDateTime postDate, long hits) {
    Set<PostSkill> sk = skillCode.stream().map(PostSkill::new).collect(Collectors.toSet());
    var post = new Post(postId, this.userId, PostState.ON_RECRUITING,
        this.title, this.describe, this.contact, this.groupType,
        this.gatherType, this.members, sk, postDate, hits);
    sk.forEach(e -> e.setPost(post));
    return post;
  }
}
