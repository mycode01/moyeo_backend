package com.justcodeit.moyeo.study.model.post;

import com.justcodeit.moyeo.study.model.type.GatherType;
import com.justcodeit.moyeo.study.model.type.GroupType;
import com.justcodeit.moyeo.study.model.type.PostState;
import com.justcodeit.moyeo.study.persistence.Post;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResMyPostDto {

  private List<MyPostDto> data;
  private int pageNo;
  private int pageSize;
  private boolean hasNext;

  public static ResMyPostDto toModel(Slice<Post> slice) {
    var data = slice.getContent().stream().map(e ->
        new MyPostDto(e.getPostId(), e.getState(), e.getTitle(),
            e.getPostDate(ZoneId.systemDefault()), e.getHits())
    ).collect(Collectors.toUnmodifiableList());

    return new ResMyPostDto(data, slice.getNumber(), slice.getSize(), slice.hasNext());
  }

  @Getter
  @AllArgsConstructor
  public static class MyPostDto {

    private String postId;
    private PostState state;
    private String title;
    private long postDate;
    private long hits;
  }

}
