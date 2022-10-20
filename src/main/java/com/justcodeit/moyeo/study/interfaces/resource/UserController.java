package com.justcodeit.moyeo.study.interfaces.resource;

import com.justcodeit.moyeo.study.application.UserService;
import com.justcodeit.moyeo.study.interfaces.dto.InquiryMyInfoResponseDto;
import com.justcodeit.moyeo.study.interfaces.dto.InquiryUserInfoResponseDto;
import com.justcodeit.moyeo.study.model.jwt.UserToken;
import com.justcodeit.moyeo.study.model.user.ReqUpdateUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

  private final UserService service;

  @Operation(summary = "유저 업데이트 ", description = "자신의 유저 정보 업데이트 ")
  @Parameter(name = "X-MOYEO-AUTH-TOKEN", in = ParameterIn.HEADER, required = true)
  @Secured("ROLE_USER")
  @PutMapping("user/me")
  public ResponseEntity<Void> updateUser(
      @Parameter(hidden = true) @AuthenticationPrincipal UserToken userToken,
      @RequestBody ReqUpdateUserDto dto) {

    service.updateUser(userToken.getUserId(), dto);

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "유저 정보 조회 ", description = "특정 유저 정보 조회 with userId ")
  @GetMapping("user/{userId}")
  public ResponseEntity<InquiryUserInfoResponseDto> inquiryUser(@PathVariable String userId) {

    var userInfo = service.getUserInfo(userId);
    var res = InquiryUserInfoResponseDto.fromUserInfo(userInfo);

    return ResponseEntity.ok(res);
  }

  @Operation(summary = "내 정보 조회 ", description = "로그인 유저 정보 조회 ")
  @Parameter(name = "X-MOYEO-AUTH-TOKEN", in = ParameterIn.HEADER, required = true)
  @Secured("ROLE_USER")
  @GetMapping("user/me")
  public ResponseEntity<InquiryMyInfoResponseDto> inquiryUser(
      @Parameter(hidden = true) @AuthenticationPrincipal UserToken userToken
  ) {

    var myInfo = service.getUserInfo(userToken.getUserId());
    var res = InquiryMyInfoResponseDto.fromUserInfo(myInfo);

    return ResponseEntity.ok(res);
  }

}
