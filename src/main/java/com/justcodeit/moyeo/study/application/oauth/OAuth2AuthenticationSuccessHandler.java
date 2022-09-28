package com.justcodeit.moyeo.study.application.oauth;

import com.justcodeit.moyeo.study.common.CookieUtils;
import com.justcodeit.moyeo.study.model.jwt.AuthTokenConverter;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  @Value("${moyeo.auth.header.scheme}")
  private String TOKEN_SCHEME;

  @Value("${moyeo.auth.redirect.whenLoginSuccess.uri}")
  private String REDIRECT_URI;

  private final AuthTokenConverter authTokenConverter;
  private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    String redirectUrl = REDIRECT_URI; // for test url, 완료시 리다이렉트 주소
//    setCookie(request, response, authentication); // 쿠키로 리턴할때
    // 쿠키로 리턴된 jwt를 프론트에서 잡아서 리퀘스트가 발생할때마다 헤더에 실어서 요청하게
    setResponseHeader(request, response, authentication); // 헤더로 리턴할때
    // 마찬가지로 헤더로 리턴된 jwt를 프론트에서 잡아서 리퀘스트가 발생할때마다 헤더에 실어 요청하게

    // 쿠키는 max-age가 다될때까지 남아있기 때문에 그보다는 헤더로 처리하는게 맞을거같음

    clearAuthenticationAttributes(request, response);
    getRedirectStrategy().sendRedirect(request, response, redirectUrl);
  }

  private void setCookie(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    String token = authTokenConverter.toTokenString(authentication);

    CookieUtils.addCookie(response, TOKEN_SCHEME, token, 60 * 60);
  }

  private void setResponseHeader(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    String token = authTokenConverter.toTokenString(authentication);

    response.setHeader(TOKEN_SCHEME, token);
  }

//  protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
//      Authentication authentication) {
//    Optional<String> redirectUri = CookieUtils.getCookie(request, "redirect_uri")
//        .map(Cookie::getValue);
//
//    String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
//
//    String token = jwtProvider.generate(authentication);
//
//    return UriComponentsBuilder.fromUriString(targetUrl)
//        .queryParam("token", token)
//        .build().toUriString();
//  } // 혹시 모든 oauth provider 가 redirect_uri 를 보낼수있도록 설정할수 있다면 이쪽 사용

  protected void clearAuthenticationAttributes(HttpServletRequest request,
      HttpServletResponse response) {
    super.clearAuthenticationAttributes(request);
    httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request,
        response);
  } // 임시 인증정보 삭제

}

