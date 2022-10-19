package com.justcodeit.moyeo.study.interfaces.resource;

import com.justcodeit.moyeo.study.application.PostFacade;
import com.justcodeit.moyeo.study.interfaces.dto.PutPostRequestDto;
import com.justcodeit.moyeo.study.model.jwt.UserToken;
import com.justcodeit.moyeo.study.model.post.ResMyPostDto;
import com.justcodeit.moyeo.study.model.post.ResPostDetailDto;
import com.justcodeit.moyeo.study.model.post.ResPostListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PostController {

  private final PostFacade facade;


  @Operation(summary = "모집글 리스트 조회 ", description = "모집글 리스트 조회 pagination ")
  @GetMapping("post")
  public ResponseEntity<ResPostListDto> getPostList(
      @Parameter(required = false) String keyword,
      @Parameter(required = false) String skillCodes,
      @Parameter(schema = @Schema(minimum = "0", defaultValue = "0")) int pageNo,
      @Parameter(schema = @Schema(minimum = "0", defaultValue = "20")) int pageSize) {

    var codes = Arrays.asList(skillCodes.split(","));
    var res = facade.findPosts(keyword, codes, pageNo, pageSize);

    return ResponseEntity.ok(res);
  }


  @Operation(summary = "모집글 상세 조회 ", description = "모집글 상세 조회 with postId ")
  @GetMapping("post/{postId}")
  public ResponseEntity<ResPostDetailDto> getPost(@PathVariable String postId) {

    var res = facade.findPost(postId);

    return ResponseEntity.ok(res);
  }


  @Operation(summary = "모집글 생성 및 수정 ", description = "모집글 생성 및 수정, postId가 null이거나 empty라면 생성 \nneitherwise 권한 확인후 업데이트처리 ")
  @Parameter(name = "X-MOYEO-AUTH-TOKEN", in = ParameterIn.HEADER, required = true)
  @Secured("ROLE_USER")
  @PutMapping("post")
  public ResponseEntity<String> createPost(
      @Parameter(hidden = true) @AuthenticationPrincipal UserToken userToken,
      @RequestBody PutPostRequestDto dto) {

    var res = facade.createPost(dto.toModel(userToken.getUserId()));

    return new ResponseEntity<>(res, HttpStatus.CREATED);
  }

  @Operation(summary = "모집글 상태 수정 ", description = "모집글 상태 수정 ")
  @Parameter(name = "X-MOYEO-AUTH-TOKEN", in = ParameterIn.HEADER, required = true)
  @Secured("ROLE_USER")
  @PatchMapping("post/{postId}")
  public ResponseEntity<String> updatePostState(
      @Parameter(hidden = true) @AuthenticationPrincipal UserToken userToken,
      @PathVariable String postId) {

    var res = facade.updateState(userToken.getUserId(), postId);

    return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
  }

  @Operation(summary = "모집글 삭제 ", description = "모집글 삭제(혹은 삭제상태로 변경) ")
  @Parameter(name = "X-MOYEO-AUTH-TOKEN", in = ParameterIn.HEADER, required = true)
  @Secured("ROLE_USER")
  @DeleteMapping("post/{postId}")
  public ResponseEntity<String> deletePost(
      @Parameter(hidden = true) @AuthenticationPrincipal UserToken userToken,
      @PathVariable String postId) {

    facade.delete(userToken.getUserId(), postId);

    return new ResponseEntity<>(postId, HttpStatus.ACCEPTED);
  }


  @Operation(summary = "나의 모집글 리스트 ", description = "내가 생성한 모집글 리스트 조회 pagination ")
  @Parameter(name = "X-MOYEO-AUTH-TOKEN", in = ParameterIn.HEADER, required = true)
  @Secured("ROLE_USER")
  @GetMapping("post/my")
  public ResponseEntity<ResMyPostDto> getMyPost(
      @Parameter(hidden = true) @AuthenticationPrincipal UserToken userToken,
      @Parameter(schema = @Schema(minimum = "0", defaultValue = "0")) int pageNo,
      @Parameter(schema = @Schema(minimum = "0", defaultValue = "20")) int pageSize) {

    var res = facade.findMyPosts(userToken.getUserId(), pageNo, pageSize);

    return ResponseEntity.ok(res);
  }
}
