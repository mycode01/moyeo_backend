package com.justcodeit.moyeo.study;

import com.justcodeit.moyeo.study.model.jwt.UserToken;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 테스트용 컨트롤러입니다.
 */
@Controller
public class DefaultController {

  @RequestMapping("/")
  public String login(Model model) {

    return "login";
  }

  @RequestMapping("/me")
  public String me(Model model, @AuthenticationPrincipal UserToken userToken) {
    // 인가 처리가 되지 않아도 진행이 가능하므로, userToken의 null 체크가 되지 않으면 500발생
    model.addAttribute("name", userToken.getUsername());
    return "loggedIn";
  }

  //  @PreAuthorize("hasRole('ROLE_USER')")
  @Secured("ROLE_USER") // 로 대체가능
  @RequestMapping("/hello")
  public String hello(Model model, @AuthenticationPrincipal UserToken userToken) {

    model.addAttribute("name", userToken.getUsername());
    return "helloworld"; // /me 와 내용은 완전히 같지만 이쪽은 인가처리가 되지 않으면 동작 x
  }
}
