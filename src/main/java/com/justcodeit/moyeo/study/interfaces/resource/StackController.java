package com.justcodeit.moyeo.study.interfaces.resource;

import io.swagger.v3.oas.annotations.Operation;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("stack")
public class StackController {

  @Operation(summary = "스택 리스트 조회 ", description = " ")
  @GetMapping
  public ResponseEntity<GetStackListResponseDto> stackList() {
    return ResponseEntity.ok(GetStackListResponseDto.buildDummy());
  }

  @Getter
  @AllArgsConstructor
  public static class StackAttrResp {

    private final String stackCode;
    private final String stackNameEn;
    private final String stackNameKr;
    private final String stackCategory;
    private final String iconUrl;

  }

  @Getter
  @AllArgsConstructor
  public static class GetStackListResponseDto {

    private final List<StackAttrResp> data;

    public static GetStackListResponseDto buildDummy() {
      var r = new ArrayList<StackAttrResp>();
      for (int i = 0; i < 300; i++) {
        r.add(
            new StackAttrResp("stackCodeString" + i, "stacknameEnString" + i,
                "stacknamekrString" + i, "stackCatstring", "www.naver.com")
        );
      }
      return new GetStackListResponseDto(r);
    }
  }
}
