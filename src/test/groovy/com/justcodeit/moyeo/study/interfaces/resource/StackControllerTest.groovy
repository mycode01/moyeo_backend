package com.justcodeit.moyeo.study.interfaces.resource

import com.fasterxml.jackson.databind.ObjectMapper
import com.justcodeit.moyeo.study.application.ScrapService
import com.justcodeit.moyeo.study.application.StackService
import com.justcodeit.moyeo.study.application.oauth.OAuth2AuthenticationSuccessHandler
import com.justcodeit.moyeo.study.application.oauth.OAuthUserService
import com.justcodeit.moyeo.study.config.ApplicationConfig
import com.justcodeit.moyeo.study.config.SecurityConfig
import com.justcodeit.moyeo.study.interfaces.dto.GetStackListResponse
import com.justcodeit.moyeo.study.model.scrap.ResMyScrapDto
import com.justcodeit.moyeo.study.model.skill.StackInfoDto
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ActiveProfiles("local")
@Import([ApplicationConfig.class, SecurityConfig.class])
@WebMvcTest(controllers = [StackController.class])
class StackControllerTest extends Specification {
    @SpringBean
    OAuthUserService oAuthUserService = Mock()
    @SpringBean
    OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler = Mock()

    @Autowired
    MockMvc mvc
    @SpringBean
    StackService service = Mock()
    @Autowired
    ObjectMapper objectMapper

    def "StackList"() {
        given:
        def expected = objectMapper.writeValueAsString(new GetStackListResponse(List.of()))
        when:
        def res = mvc.perform(get("/stack")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)) // deprecate 되었지만, utf8을 사용하지 않으면 한글이 깨짐

        then:
        res.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expected))
        1 * service.getStackList() >> List<StackInfoDto>.of()

    }
}
