package com.justcodeit.moyeo.study.interfaces.resource

import com.fasterxml.jackson.databind.ObjectMapper
import com.justcodeit.moyeo.study.application.JwtProvider
import com.justcodeit.moyeo.study.application.PostService
import com.justcodeit.moyeo.study.application.ScrapService
import com.justcodeit.moyeo.study.application.oauth.OAuth2AuthenticationSuccessHandler
import com.justcodeit.moyeo.study.application.oauth.OAuthUserService
import com.justcodeit.moyeo.study.config.ApplicationConfig
import com.justcodeit.moyeo.study.config.SecurityConfig
import com.justcodeit.moyeo.study.model.post.ResMyPostDto
import com.justcodeit.moyeo.study.model.scrap.ResMyScrapDto
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@ActiveProfiles("local")
@Import([ApplicationConfig.class, SecurityConfig.class])
@WebMvcTest(controllers = [ScrapController.class])
class ScrapControllerTest extends Specification {
    @SpringBean
    OAuthUserService oAuthUserService = Mock()
    @SpringBean
    OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler = Mock()

    @Autowired
    MockMvc mvc
    @SpringBean
    ScrapService service = Mock()
    @Autowired
    ObjectMapper objectMapper
    @Autowired
    JwtProvider jwtProvider

    def "MyScrapList"() {
        given:
        def jwtString = jwtProvider.generate(
                Map.of("userId", userId, "nickname", "username", "email", "email", "role", "ROLE_USER"))
        def cont = objectMapper.writeValueAsString(new ResMyScrapDto(List.of(), pageNo as int, pageSize as int, false))

        when:
        def res = mvc.perform(get("/scrap")
                .param("pageNo", pageNo)
                .param("pageSize", pageSize)
                .header("x-moyeo-auth-token", jwtString)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)) // deprecate 되었지만, utf8을 사용하지 않으면 한글이 깨짐

        then:
        res.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(cont))
        1 * service.getMyScraps(_, _, _) >> new ResMyScrapDto(List.of(), pageNo as int, pageSize as int, false)


        where:
        userId     | pageNo | pageSize
        "fdf11123" | "0"    | "10"
        "xxxxxx"   | "2"    | "5"
        "f11123"   | "100"  | "1"
    }

    def "Scrap"() {
        given:
        def jwtString = jwtProvider.generate(
                Map.of("userId", userId, "nickname", "username", "email", "email", "role", "ROLE_USER"))

        when:
        def res = mvc.perform(put("/scrap/"+postId)
                .header("x-moyeo-auth-token", jwtString)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)) // deprecate 되었지만, utf8을 사용하지 않으면 한글이 깨짐

        then:
        res.andExpect(status().isAccepted())
        res.andReturn().getResponse().getContentAsString() == postId
        1 * service.doScrap(_, _) >> {}


        where:
        userId     | postId
        "fdf11123" | "1fdddas"
        "xxxxxx"   | "fdvvx"
        "f11123"   | "dfwss"
    }

    def "DeleteScrap"() {
        given:
        def jwtString = jwtProvider.generate(
                Map.of("userId", userId, "nickname", "username", "email", "email", "role", "ROLE_USER"))

        when:
        def res = mvc.perform(delete("/scrap/"+postId)
                .header("x-moyeo-auth-token", jwtString)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)) // deprecate 되었지만, utf8을 사용하지 않으면 한글이 깨짐

        then:
        res.andExpect(status().isAccepted())
        res.andReturn().getResponse().getContentAsString() == postId
        1 * service.undoScrap(_, _) >> {}


        where:
        userId     | postId
        "fdf11123" | "1fdddas"
        "xxxxxx"   | "fdvvx"
        "f11123"   | "dfwss"
    }
}
