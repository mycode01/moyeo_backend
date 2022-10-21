package com.justcodeit.moyeo.study.interfaces.resource

import com.fasterxml.jackson.databind.ObjectMapper
import com.justcodeit.moyeo.study.application.JwtProvider
import com.justcodeit.moyeo.study.application.UserService
import com.justcodeit.moyeo.study.application.oauth.OAuth2AuthenticationSuccessHandler
import com.justcodeit.moyeo.study.application.oauth.OAuthUserService
import com.justcodeit.moyeo.study.config.ApplicationConfig
import com.justcodeit.moyeo.study.config.SecurityConfig
import com.justcodeit.moyeo.study.interfaces.dto.InquiryMyInfoResponse
import com.justcodeit.moyeo.study.interfaces.dto.PutPostRequest
import com.justcodeit.moyeo.study.model.type.GatherType
import com.justcodeit.moyeo.study.model.type.GroupType
import com.justcodeit.moyeo.study.model.user.ReqUpdateUserDto
import com.justcodeit.moyeo.study.model.user.UserInfoDto
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ActiveProfiles("local")
@Import([ApplicationConfig.class, SecurityConfig.class])
@WebMvcTest(controllers = [UserController.class])
class UserControllerTest extends Specification {
    @SpringBean
    OAuthUserService oAuthUserService = Mock()
    @SpringBean
    OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler = Mock()

    @Autowired
    MockMvc mvc
    @SpringBean
    UserService service = Mock()
    @Autowired
    ObjectMapper objectMapper
    @Autowired
    JwtProvider jwtProvider

    def "UpdateUser"() {
        given:
        def dto = new ReqUpdateUserDto("nickname", "introduce", Set.of())
        def cont = objectMapper.writeValueAsString(dto)
        def jwtString = jwtProvider.generate(
                Map.of("userId", "userId", "nickname", "username", "email", "email", "role", "ROLE_USER"))

        when:
        def res = mvc.perform(put("/user/me")
                .header("X-MOYEO-AUTH-TOKEN", jwtString)
                .content(cont).accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        then:
        res.andExpect(status().isNoContent())
        1 * service.updateUser(_, _) >> {}

    }

    def "InquiryMe"() {
        given:
        def jwtString = jwtProvider.generate(
                Map.of("userId", userId, "nickname", "username", "email", "email", "role", "ROLE_USER"))

        when:
        def res = mvc.perform(get("/user/me")
                .header("X-MOYEO-AUTH-TOKEN", jwtString)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        then:
        res.andExpect(status().isOk())
        res.andReturn().getResponse().getContentAsString().contains(userId)
        1 * service.getUserInfo(userId) >> new UserInfoDto(userId, "nickname", "email", "introduce", "pic", Set.of())

        where:
        userId << ["user123", "dfdfwwww"]
    }

    def "InquiryUser"() {
        given:
        when:
        def res = mvc.perform(get("/user/"+userId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        then:
        res.andExpect(status().isOk())
        res.andReturn().getResponse().getContentAsString().contains(userId)
        1 * service.getUserInfo(userId) >> new UserInfoDto(userId, "nickname", "email", "introduce", "pic", Set.of())

        where:
        userId << ["user123", "dfdfwwww"]
    }
}
