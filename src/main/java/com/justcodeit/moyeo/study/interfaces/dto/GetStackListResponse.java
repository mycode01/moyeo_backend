package com.justcodeit.moyeo.study.interfaces.dto;

import com.justcodeit.moyeo.study.model.skill.StackInfoDto;
import java.util.Collections;
import java.util.List;
import lombok.Getter;

@Getter
public class GetStackListResponse {
  private final List<StackInfoDto> data;

  public GetStackListResponse(List<StackInfoDto> data) {
    this.data = Collections.unmodifiableList(data);
  }
}
