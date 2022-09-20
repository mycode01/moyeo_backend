package com.justcodeit.moyeo.study.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Value("${moyeo.auth.header.scheme}")
  private String TOKEN_SCHEME;

  @Bean
  public AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.authorizeRequests()
        .antMatchers("/v3/**").permitAll() // swagger-ui 관련,
        .antMatchers("/v1/**").permitAll() // swagger-ui 관련,
        .antMatchers("/swagger-ui/**").permitAll()
//        .antMatchers("/login/**").permitAll()
//        .antMatchers("/**").authenticated();
        .antMatchers("/").permitAll(); // 추후 설정합시다
    // jwt 토큰 생성에 대한 부분 필요함

    http.headers().defaultsDisabled().contentTypeOptions();
    http.headers().frameOptions().disable().xssProtection().block(true);

    http.formLogin().disable()
        .logout().disable()
        .cors().and().csrf().disable();
//        .oauth2Login()
//        ;
//        .userInfoEndpoint().userService(oAuthUserService) // oauth 로그인 이후 처리
//        .and()
//        .defaultSuccessUrl("/logged-in");
  }
}
