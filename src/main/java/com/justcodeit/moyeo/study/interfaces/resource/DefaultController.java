package com.justcodeit.moyeo.study.interfaces.resource;

import com.justcodeit.moyeo.study.interfaces.dto.post.PostUserResponseDto;
import com.justcodeit.moyeo.study.model.jwt.UserToken;
import com.justcodeit.moyeo.study.persistence.Post;
import com.justcodeit.moyeo.study.persistence.User;
import com.justcodeit.moyeo.study.persistence.repository.PostCustomRepository;
import com.justcodeit.moyeo.study.persistence.repository.PostJPARepository;
import com.justcodeit.moyeo.study.persistence.repository.UserCustomRepository;
import com.justcodeit.moyeo.study.persistence.repository.UserJPARepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 테스트용 컨트롤러입니다.
 */
@RequiredArgsConstructor
@RestController
public class DefaultController {

  private final UserCustomRepository userCustomRepository;
  private final PostCustomRepository postCustomRepository;
  private final UserJPARepository userJPARepository;
  private final PostJPARepository postRepository;

  @GetMapping("hello")
  public String hello() {
    return "hello world";
  }

  @GetMapping("hi")
  public List<User> hello2() {
    return userCustomRepository.findByName("hi2");
  }

  @PostMapping("/member")
  public User createMember(){
    long count = userJPARepository.count();
    String username = "testUser"+count;
    String email = "testUser"+count+"@gmail.com";
    User user = new User(username, email, null, User.Role.USER);
    return userJPARepository.save(user);
  }

  @PostMapping("/post1")
  public Post createPost(){
    long count = postRepository.count();
    String title = "testTitle"+ count;
    String contents = "testTitle"+ count+"'s Contens";
    User user = userJPARepository.findById(count+1).get();
    Post post = new Post(title, contents, user);

    return postRepository.save(post);
  }

  @GetMapping("/join-test")
  public List<PostUserResponseDto> getJoinedPost(){
    List<Post> userPosts = postCustomRepository.getUserPosts();
    List<PostUserResponseDto> responseDtos = new ArrayList<>();

    // 무한 순환 참조 떄문에 Dto로 만들어서 전달. Mapstruct를 미사용.
    for (Post post : userPosts) {
      responseDtos.add(new PostUserResponseDto(post));
    }

    return responseDtos;
  }

  @Controller
  public static class LoginTestController {

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
}
