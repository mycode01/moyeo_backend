package com.justcodeit.moyeo.study.interfaces.resource;

import com.justcodeit.moyeo.study.model.jwt.UserToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("scrap")
@RestController
public class ScrapController {

  @Operation(summary = "나의 스크랩 목록 ", description = "유저가 스크랩 해둔 스터디 목록 ")
  @Parameter(name = "X-MOYEO-AUTH-TOKEN", in = ParameterIn.HEADER, required = true)
  @Secured("ROLE_USER")
  @GetMapping
  public ResponseEntity<Scraps> myScrapList(
      @Parameter(hidden = true) @AuthenticationPrincipal UserToken userToken) {
    var scraps = new Scraps(List.of("postIdstring1", "postIdstring2", "postIdstring3"));
    return ResponseEntity.ok(scraps);
  }

  @Operation(summary = "스크랩 하기 ", description = "유저가 스크랩을 시도 ")
  @Parameter(name = "X-MOYEO-AUTH-TOKEN", in = ParameterIn.HEADER, required = true)
  @Secured("ROLE_USER")
  @PostMapping
  public ResponseEntity<String> scrap(
      @Parameter(hidden = true) @AuthenticationPrincipal UserToken userToken,
      String postId) {
    return new ResponseEntity<>(postId, HttpStatus.ACCEPTED);
  }

  @Getter
  @AllArgsConstructor
  public static class Scraps {

    private final List<String> postIds;
  }
}
