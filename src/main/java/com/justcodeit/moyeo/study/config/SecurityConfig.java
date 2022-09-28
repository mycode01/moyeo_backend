package com.justcodeit.moyeo.study.config;

import com.justcodeit.moyeo.study.application.oauth.OAuth2AuthenticationSuccessHandler;
import com.justcodeit.moyeo.study.application.oauth.OAuthUserService;
import com.justcodeit.moyeo.study.config.entrypoint.AuthenticationEntryPoint;
import com.justcodeit.moyeo.study.config.filter.TokenAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final OAuthUserService oAuthUserService;
  private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
  private final TokenAuthFilter tokenAuthFilter;

  @Bean
  public AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .addFilterAfter(tokenAuthFilter,
            UsernamePasswordAuthenticationFilter.class) // request 발생시 auth 필터
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 세션 정보 x

    http.authorizeRequests()
        .antMatchers("/v3/**").permitAll() // swagger-ui 관련,
        .antMatchers("/v1/**").permitAll() // swagger-ui 관련,
        .antMatchers("/swagger-ui/**").permitAll()
//        .antMatchers("/**").authenticated();
        .antMatchers("/").permitAll(); // 추후 설정합시다

    http.headers().defaultsDisabled().contentTypeOptions();
    http.headers().frameOptions().disable().xssProtection().block(true);

    http.formLogin().disable()
        .logout().disable()
        .cors().and().csrf().disable()
        .exceptionHandling()
        .authenticationEntryPoint(new AuthenticationEntryPoint()) // 인증문제 발생시 처리
        .and()
        .oauth2Login()
        .userInfoEndpoint().userService(oAuthUserService) //oauth 인증후 처리
        .and()
        .successHandler(oAuth2AuthenticationSuccessHandler);
//        .failureHandler();// 그럴일은 없어보이지만, 필요한 정보가 선택적일때 누락되어 보내질수 있으므로 작성할때는 이쪽에.

  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
  } // 없으면 스프링 시큐리티의 버그로 @SpringBootTest 시 발광을 하기 때문에 추가
}
