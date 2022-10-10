package com.justcodeit.moyeo.study.interfaces.resource;

import com.justcodeit.moyeo.study.model.jwt.UserToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @Operation(summary = "유저 업데이트 ", description = "자신의 유저 정보 업데이트 ")
  @Parameter(name = "X-MOYEO-AUTH-TOKEN", in = ParameterIn.HEADER, required = true)
  @Secured("ROLE_USER")
  @PutMapping("user/me")
  public ResponseEntity<Void> updateUser(
      @Parameter(hidden = true) @AuthenticationPrincipal UserToken userToken,
      @RequestBody UpdateUserRequestDto dto) {
    return ResponseEntity.noContent().build(); // 업데이트용
  }

  @Operation(summary = "유저 정보 조회 ", description = "특정 유저 정보 조회 with userId ")
  @GetMapping("user/{userId}")
  public ResponseEntity<InquiryUserResponseDto> inquiryUser(@PathVariable String userId) {
    return ResponseEntity.ok(
        new InquiryUserResponseDto("userIdstring", "nicknamestring", "emailstring",
            "introducestring", List.of("python", "java", "or skill Code not nameString")));
  }

  @Getter
  @AllArgsConstructor
  public static class UpdateUserRequestDto {

    private final String nickname;
    private final String email;
    private final String introduce;
    private final List<String> skills;
  }

  @Getter
  @AllArgsConstructor
  public static class InquiryUserResponseDto {

    private final String userId;
    private final String nickname;
    private final String email;
    private final String introduce;
    private final List<String> skills;
  }
}
