package com.justcodeit.moyeo.study.model.post;


import com.justcodeit.moyeo.study.model.type.PostState;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.Getter;

@Getter
public class QueryPostsProjection {
  private String postId;
  private PostState state;
  private String title;
  private LocalDateTime postDate;
  private long hits;

  public long getPostDate(ZoneId zoneId) {
    return postDate.atZone(zoneId).toEpochSecond();
  }

  @QueryProjection
  public QueryPostsProjection(String postId, PostState state, String title, LocalDateTime postDate,
      long hits) {
    this.postId = postId;
    this.state = state;
    this.title = title;
    this.postDate = postDate;
    this.hits = hits;
  }
}