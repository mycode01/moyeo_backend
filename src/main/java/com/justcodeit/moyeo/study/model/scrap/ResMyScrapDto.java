package com.justcodeit.moyeo.study.model.scrap;

import com.justcodeit.moyeo.study.persistence.Scrap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
@AllArgsConstructor
public class ResMyScrapDto {

  private List<String> data;
  private int pageNo;
  private int pageSize;
  private boolean hasNext;

  public static ResMyScrapDto toModel(Slice<Scrap> slice) {
    var data = slice.getContent().stream().map(Scrap::getPostId).collect(Collectors.toList());
    return new ResMyScrapDto(data, slice.getNumber(), slice.getSize(), slice.hasNext());
  }
}
