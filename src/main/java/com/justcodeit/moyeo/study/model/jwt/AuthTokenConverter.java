package com.justcodeit.moyeo.study.model.jwt;

import com.justcodeit.moyeo.study.application.JwtProvider;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

@RequiredArgsConstructor
public class AuthTokenConverter {

  private final JwtProvider jwt;

  public UserToken fromToken(String t) {
    var claims = jwt.claims(t);
    var n = claims.get("username", String.class);
    var r = claims.get("role", String.class);
    return new UserToken(r, n); // todo 토큰이 변경되면 같이 수정되어야 함
  }

  public String toTokenString(Authentication auth) {
    Map<String, Object> claims = new HashMap<>() {{
      put("username", auth.getName());
      put("role", auth.getAuthorities().stream().findFirst().get().toString());
    }};

    return jwt.generate(claims);
  }

}

