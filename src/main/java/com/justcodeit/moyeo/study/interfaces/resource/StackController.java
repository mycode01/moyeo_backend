package com.justcodeit.moyeo.study.interfaces.resource;

import com.justcodeit.moyeo.study.application.StackService;
import com.justcodeit.moyeo.study.interfaces.dto.GetStackListResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("stack")
public class StackController {

  private final StackService service;

  @Operation(summary = "스택 리스트 조회 ", description = " ")
  @GetMapping
  public ResponseEntity<GetStackListResponse> stackList() {
    var res = new GetStackListResponse(service.getStackList());
    return ResponseEntity.ok(res);
  }
}
