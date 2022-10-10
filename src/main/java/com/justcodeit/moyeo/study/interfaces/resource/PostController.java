package com.justcodeit.moyeo.study.interfaces.resource;

import com.justcodeit.moyeo.study.model.jwt.UserToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

  @Operation(summary = "모집글 리스트 조회 ", description = "모집글 리스트 조회 pagination ")
  @GetMapping("post")
  public ResponseEntity<GetPostListResponseDto> getPostList(
      @Parameter(required = false) String keyword,
      @Parameter(required = false) String skillCodes,
      @Parameter(schema = @Schema(minimum = "0", defaultValue = "0")) int pageNo,
      @Parameter(schema = @Schema(minimum = "0", defaultValue = "20")) int pageSize) {
    return ResponseEntity.ok(
        new GetPostListResponseDto(PostAttrResp.buildDummy(), pageNo, pageSize, true));
  }


  @Operation(summary = "모집글 상세 조회 ", description = "모집글 상세 조회 with postId ")
  @GetMapping("post/{postId}")
  public ResponseEntity<GetPostResponseDto> getPost(@PathVariable String postId) {
    var member = new RecruitMember();
    member.addMember("백엔드", 2);
    member.addMember("프론트엔드", 2);
    var skills = List.of("vue", "spring", "or skill Code not nameString");
    return ResponseEntity.ok(
        new GetPostResponseDto("postIdstring", "titlestring", "describestring",
            "스터디", "온라인", "nowhere@void.com", skills, member));
  }


  @Operation(summary = "모집글 생성 및 수정 ", description = "모집글 생성 및 수정, postId가 null이거나 empty라면 생성 \nneitherwise 권한 확인후 업데이트처리 ")
  @Parameter(name = "X-MOYEO-AUTH-TOKEN", in = ParameterIn.HEADER, required = true)
  @Secured("ROLE_USER")
  @PostMapping("post")
  public ResponseEntity<String> createPost(
      @Parameter(hidden = true) @AuthenticationPrincipal UserToken userToken,
      @RequestBody PutPostRequestDto dto) {
    return new ResponseEntity("postIdstring", HttpStatus.CREATED);
  }

  @Operation(summary = "모집글 상태 수정 ", description = "모집글 상태 수정 ")
  @Parameter(name = "X-MOYEO-AUTH-TOKEN", in = ParameterIn.HEADER, required = true)
  @Secured("ROLE_USER")
  @PatchMapping("post/{postId}")
  public ResponseEntity<String> updatePostState(
      @Parameter(hidden = true) @AuthenticationPrincipal UserToken userToken,
      @PathVariable String postId) {
    return new ResponseEntity<String>(postId, HttpStatus.ACCEPTED);
  }

  @Operation(summary = "모집글 삭제 ", description = "모집글 삭제(혹은 삭제상태로 변경) ")
  @Parameter(name = "X-MOYEO-AUTH-TOKEN", in = ParameterIn.HEADER, required = true)
  @Secured("ROLE_USER")
  @DeleteMapping("post/{postId}")
  public ResponseEntity<String> deletePost(
      @Parameter(hidden = true) @AuthenticationPrincipal UserToken userToken,
      @PathVariable String postId) {
    return new ResponseEntity<String>(postId, HttpStatus.ACCEPTED);
  }


  @Operation(summary = "나의 모집글 리스트 ", description = "내가 생성한 모집글 리스트 조회 pagination ")
  @Parameter(name = "X-MOYEO-AUTH-TOKEN", in = ParameterIn.HEADER, required = true)
  @Secured("ROLE_USER")
  @GetMapping("post/my")
  public ResponseEntity<GetPostListResponseDto> getMyPost(
      @Parameter(schema = @Schema(minimum = "0", defaultValue = "0")) int pageNo,
      @Parameter(schema = @Schema(minimum = "0", defaultValue = "20")) int pageSize) {

    return ResponseEntity.ok(
        new GetPostListResponseDto(PostAttrResp.buildDummy(), pageNo, pageSize, true));
  }


  @Getter
  @AllArgsConstructor
  public static class PostAttrResp {

    private final String postId;
    private final String title;
    private final List<String> skills;
    private final long regDateEpoch;
    private final long hits;

    public static List<PostAttrResp> buildDummy() {
      List<PostAttrResp> r = new ArrayList<>();
      for (int i = 0; i < 20; i++) {
        r.add(
            new PostAttrResp("postIdstring" + i, "웹 프론트엔드 팀원 모집합니다.",
                List.of("javscript", "react", "python", "or skill Code not nameString"),
                Instant.now().getEpochSecond(), 33)
        );
      }
      return r;
    }
  }

  @Getter
  @AllArgsConstructor
  public static class GetPostListResponseDto {

    private final List<PostAttrResp> data;
    private final int pageNo;
    private final int pageSize;
    private final boolean hasNext;
  }

  @Getter
  @AllArgsConstructor
  public static class PutPostRequestDto {

    private final String postId;
    private final String title;
    private final String describe;
    private final String recruitType;
    private final String progressMethod;
    private final String contact;
    private final List<String> skillCode;
    private final Map<String, Integer> members;

  }

  @Getter
  @AllArgsConstructor
  public static class GetPostResponseDto {

    private final String postId;
    private final String title;
    private final String describe;
    private final String recruitType;
    private final String progressMethod;
    private final String contact;
    private final List<String> skills;
    private final RecruitMember members;

  }

  @Getter
  public static class RecruitMember {

    private final Map<String, Integer> member;

    public void addMember(String t, int number) {
      member.computeIfPresent(t, (k, v) -> v + number);
    }

    public RecruitMember() {
      this.member = new HashMap<>();
    }
  }
}
