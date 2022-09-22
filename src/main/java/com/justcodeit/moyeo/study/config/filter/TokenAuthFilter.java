package com.justcodeit.moyeo.study.config.filter;

import com.justcodeit.moyeo.study.model.jwt.AuthTokenConverter;
import com.justcodeit.moyeo.study.model.jwt.UserToken;
import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class TokenAuthFilter extends OncePerRequestFilter {

  private final String TOKEN_SCHEME;
  private final AuthTokenConverter tokenConverter;

  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String tokenString = request.getHeader(this.TOKEN_SCHEME);
    if (!this.isEmptyString(tokenString)) {
      UserToken userToken = tokenConverter.fromToken(tokenString);
      List<GrantedAuthority> auths = AuthorityUtils.createAuthorityList(
          new String[]{userToken.getRole()});
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userToken,
          (Object) null, auths);
      SecurityContextHolder.getContext().setAuthentication(auth);
    }

    filterChain.doFilter(request, response);
  }

  boolean isEmptyString(String string) {
    return string == null || string.isEmpty();
  }
}