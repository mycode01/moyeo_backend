package com.justcodeit.moyeo.study.config.entrypoint;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;

@Slf4j
public class AuthenticationEntryPoint implements
    org.springframework.security.web.AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      AuthenticationException e) throws IOException, ServletException {
    log.error("Responding with unauthorized error. Message - {}", e.getMessage());
    httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
        e.getLocalizedMessage()); // todo 프론트와의 메시지 포멧을 통합할 필요가 있어보임
  }
}
