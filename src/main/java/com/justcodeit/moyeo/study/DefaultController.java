package com.justcodeit.moyeo.study;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 테스트용 컨트롤러입니다.
 */
@RestController
public class DefaultController {
  @GetMapping("hello")
  public String hello(){
    return "hello world";
  }

}
