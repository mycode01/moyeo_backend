package com.justcodeit.moyeo.study.model.post;

import com.justcodeit.moyeo.study.model.type.PostState;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResPostListDto {


  private List<PostData> data;
  private int pageNo;
  private int pageSize;
  private boolean hasNext;

  public static ResPostListDto toModel(Slice<QueryPostsProjection> slice) {
    var data = slice.getContent().stream().map(e ->
        new PostData(e.getPostId(), e.getState(), e.getTitle(), e.getSkillCodes(),
            e.getPostDate(ZoneId.systemDefault()), e.getHits())).collect(Collectors.toList());
    return new ResPostListDto(data, slice.getNumber(), slice.getSize(), slice.hasNext());
  }

  @Getter
  @AllArgsConstructor
  public static class PostData {

    private String postId;
    private PostState state;
    private String title;
    private List<String> skillCodes;
    private long postDate;
    private long hits;
  }
}
