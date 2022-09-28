package com.justcodeit.moyeo.study.config;

import com.justcodeit.moyeo.study.application.JwtProvider;
import com.justcodeit.moyeo.study.config.filter.TokenAuthFilter;
import com.justcodeit.moyeo.study.model.jwt.AuthTokenConverter;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

@Configuration
public class ApplicationConfig {

  @Value("${moyeo.auth.header.scheme}")
  private String TOKEN_SCHEME;
  @Value("${moyeo.jwt.secret}")
  private String secretKey;
  @Value("${moyeo.jwt.expiryMills}")
  private long expireMills;

  @Bean
  public JwtBuilder jwtBuilder() {
    return Jwts.builder().signWith(SignatureAlgorithm.HS256, secretKey);
  }

  @Bean
  public JwtParser jwtParser() {
    return Jwts.parserBuilder().setSigningKey(secretKey).build();
  }

  @Bean
  public JwtProvider jwtProvider(JwtParser parser, JwtBuilder builder) {
    return new JwtProvider(expireMills, builder, parser);
  }

  @Bean
  public AuthTokenConverter authTokenConverter(JwtProvider jwtProvider) {
    return new AuthTokenConverter(jwtProvider);
  }

  @Bean
  public TokenAuthFilter tokenAuthFilter(AuthTokenConverter authTokenConverter) {
    return new TokenAuthFilter(TOKEN_SCHEME, authTokenConverter);
  }

  @Bean
  public DefaultOAuth2UserService defaultOAuth2UserService() {
    return new DefaultOAuth2UserService();
  }
}

