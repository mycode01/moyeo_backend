package com.justcodeit.moyeo.study.interfaces.resource;

import com.justcodeit.moyeo.study.application.ScrapService;
import com.justcodeit.moyeo.study.model.jwt.UserToken;
import com.justcodeit.moyeo.study.model.scrap.ResMyScrapDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("scrap")
@RestController
public class ScrapController {

  private final ScrapService service;

  @Operation(summary = "나의 스크랩 목록 ", description = "유저가 스크랩 해둔 스터디 목록 ")
  @Parameter(name = "X-MOYEO-AUTH-TOKEN", in = ParameterIn.HEADER, required = true)
  @Secured("ROLE_USER")
  @GetMapping
  public ResponseEntity<ResMyScrapDto> myScrapList(
      @Parameter(hidden = true) @AuthenticationPrincipal UserToken userToken,
      @Parameter(schema = @Schema(minimum = "0", defaultValue = "0")) int pageNo,
      @Parameter(schema = @Schema(minimum = "0", defaultValue = "20")) int pageSize) {

    var res = service.getMyScraps(userToken.getUserId(), pageNo, pageSize);

    return ResponseEntity.ok(res);
  }

  @Operation(summary = "스크랩 하기 ", description = "유저가 스크랩을 시도 ")
  @Parameter(name = "X-MOYEO-AUTH-TOKEN", in = ParameterIn.HEADER, required = true)
  @Secured("ROLE_USER")
  @PutMapping("{postId}")
  public ResponseEntity<String> scrap(
      @Parameter(hidden = true) @AuthenticationPrincipal UserToken userToken,
      @PathVariable String postId) {

    service.doScrap(userToken.getUserId(), postId);

    return new ResponseEntity<>(postId, HttpStatus.ACCEPTED);
  }


  @Operation(summary = "스크랩 삭제 ", description = "유저의 스크랩 항목을 삭제 ")
  @Parameter(name = "X-MOYEO-AUTH-TOKEN", in = ParameterIn.HEADER, required = true)
  @Secured("ROLE_USER")
  @DeleteMapping("{postId}")
  public ResponseEntity<String> deleteScrap(
      @Parameter(hidden = true) @AuthenticationPrincipal UserToken userToken,
      @PathVariable String postId) {

    service.undoScrap(userToken.getUserId(), postId);

    return new ResponseEntity<>(postId, HttpStatus.ACCEPTED);
  }

}
