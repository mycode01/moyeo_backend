package com.justcodeit.moyeo.study.model.post;


import com.justcodeit.moyeo.study.model.type.PostState;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import lombok.Getter;

@Getter
public class QueryPostsProjection {

  private String postId;
  private PostState state;
  private String title;

  private List<String> skillCodes;
  private LocalDateTime postDate;
  private long hits;

  public long getPostDate(ZoneId zoneId) {
    return postDate.atZone(zoneId).toEpochSecond();
  }

  private QueryPostsProjection(String postId, PostState state, String title, List<String> skillCodes,
      LocalDateTime postDate, long hits) {
    this.postId = postId;
    this.state = state;
    this.title = title;
    this.skillCodes = skillCodes;
    this.postDate = postDate;
    this.hits = hits;
  }

  public static class ProjectionBuilder {

    private String postId;
    private PostState state;
    private String title;
    private List<String> skillCodes;
    private LocalDateTime postDate;
    private long hits;

    @QueryProjection
    public ProjectionBuilder(String postId, PostState state, String title, LocalDateTime postDate,
        long hits) {
      this.postId = postId;
      this.state = state;
      this.title = title;
      this.postDate = postDate;
      this.hits = hits;
      this.skillCodes = Collections.emptyList();
    }

    public ProjectionBuilder skillCodes(List<String> skillCodes) {
      this.skillCodes = skillCodes;
      return this;
    }

    public QueryPostsProjection build() {
      return new QueryPostsProjection(this.postId, this.state, this.title, this.skillCodes,
          this.postDate, this.hits);
    }

    public String getPostId() {
      return this.postId;
    }
  }

  @Getter
  public static class PostSkillProjection {
    private String postId;
    private String skillCode;

    @QueryProjection
    public PostSkillProjection(String postId, String skillCode) {
      this.postId = postId;
      this.skillCode = skillCode;
    }
  }

}