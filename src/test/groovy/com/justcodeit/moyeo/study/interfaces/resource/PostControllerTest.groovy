package com.justcodeit.moyeo.study.interfaces.resource

import com.fasterxml.jackson.databind.ObjectMapper
import com.justcodeit.moyeo.study.application.JwtProvider
import com.justcodeit.moyeo.study.application.PostService
import com.justcodeit.moyeo.study.application.oauth.OAuth2AuthenticationSuccessHandler
import com.justcodeit.moyeo.study.application.oauth.OAuthUserService
import com.justcodeit.moyeo.study.config.ApplicationConfig
import com.justcodeit.moyeo.study.config.SecurityConfig
import com.justcodeit.moyeo.study.interfaces.dto.PutPostRequest
import com.justcodeit.moyeo.study.model.post.ResMyPostDto
import com.justcodeit.moyeo.study.model.post.ResPostDetailDto
import com.justcodeit.moyeo.study.model.post.ResPostListDto
import com.justcodeit.moyeo.study.model.type.GatherType
import com.justcodeit.moyeo.study.model.type.GroupType
import com.justcodeit.moyeo.study.model.type.PostState
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ActiveProfiles("local")
@Import([ApplicationConfig.class, SecurityConfig.class])
@WebMvcTest(controllers = [PostController.class])
class PostControllerTest extends Specification {

    @SpringBean
    OAuthUserService oAuthUserService = Mock()
    @SpringBean
    OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler = Mock()

    @Autowired
    MockMvc mvc
    @SpringBean
    PostService service = Mock()
    @Autowired
    ObjectMapper objectMapper
    @Autowired
    JwtProvider jwtProvider

    @Unroll
    def "GetPostList"() {
        given:
        when:
        def res = mvc.perform(get("/post")
                .param("keyword", keyword)
                .param("skillCodes", skillcodes)
                .param("pageNo", pageNo)
                .param("pageSize", pageSize)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)) // deprecate 되었지만, utf8을 사용하지 않으면 한글이 깨짐

        then:
        def cont = objectMapper.writeValueAsString(new ResPostListDto(List.of(), pageNo as int, pageSize as int, false))
        res.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(cont))
        1 * service.findPosts(_, _, _, _) >> new ResPostListDto(List.of(), pageNo as int, pageSize as int, false)


        where:
        keyword | skillcodes                         | pageNo | pageSize
        "title" | ""                                 | "0"    | "20"
        ""      | "skillcode1,skillcode2,skillcode3" | "0"    | "20"
        ""      | ""                                 | "0"    | "20"
    }

    def "GetPost"() {
        given:
        def expected = new ResPostDetailDto(postId, "", PostState.ON_PROGRESS, "title", "content", "contact", 0, GroupType.BASIC_STUDY, GatherType.OFFLINE, Set.of(), Map.of())

        when:
        def res = mvc.perform(get("/post/" + postId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)) // deprecate 되었지만, utf8을 사용하지 않으면 한글이 깨짐

        then:
        res.andExpect(status().isOk())
        res.andReturn().getResponse().getContentAsString().contains(postId)
        1 * service.findPost(_) >> expected


        where:
        postId << ["postid1", "postid3", "postid2"]
    }

    def "CreatePost"() {
        given:
        def dto = new PutPostRequest(postId, "title", "desc", GroupType.BASIC_STUDY, GatherType.OFFLINE, "contact", List.of(), Map.of())
        def cont = objectMapper.writeValueAsString(dto)
        def jwtString = jwtProvider.generate(
                Map.of("userId", userId, "nickname", "username", "email", "email", "role", "ROLE_USER"))
        def resPostId = ""

        when:
        def res = mvc.perform(put("/post")
                .header("X-MOYEO-AUTH-TOKEN", jwtString)
                .content(cont).accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        then:
        res.andExpect(status().isCreated())
        if (postId == "") {
            resPostId = "generatedPostId"
            1 * service.createPost(_) >> resPostId
            0 * service.updatePost(_)
        } else {
            resPostId = postId
            0 * service.createPost(_)
            1 * service.updatePost(_) >> resPostId
        }
        res.andReturn().getResponse().getContentAsString().contains(resPostId)

        where:
        userId     | postId
        "132fdsfd" | "postId123123"
        "132fdsfd" | ""
    }

    def "UpdatePostState"() {
        given:
        def jwtString = jwtProvider.generate(
                Map.of("userId", userId, "nickname", "username", "email", "email", "role", "ROLE_USER"))

        when:
        def res = mvc.perform(patch("/post/" + postId)
                .header("X-MOYEO-AUTH-TOKEN", jwtString)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        then:
        res.andExpect(status().isAccepted())
        1 * service.updateState(_, _) >> postId
        res.andReturn().getResponse().getContentAsString().contains(postId)

        where:
        userId     | postId
        "132fdsfd" | "postId123123"
        "gggg"     | "dfdfdf1111"
    }

    def "DeletePost"() {
        given:
        def jwtString = jwtProvider.generate(
                Map.of("userId", userId, "nickname", "username", "email", "email", "role", "ROLE_USER"))

        when:
        def res = mvc.perform(delete("/post/" + postId)
                .header("X-MOYEO-AUTH-TOKEN", jwtString)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
        then:
        res.andExpect(status().isAccepted())
        1 * service.delete(_, _) >> {}
        res.andReturn().getResponse().getContentAsString().contains(postId)

        where:
        userId     | postId
        "132fdsfd" | "postId123123"
        "gggg"     | "dfdfdf1111"
    }

    def "GetMyPost"() {
        given:
        def jwtString = jwtProvider.generate(
                Map.of("userId", userId, "nickname", "username", "email", "email", "role", "ROLE_USER"))
        def cont = objectMapper.writeValueAsString(new ResMyPostDto(List.of(), pageNo as int, pageSize as int, false))

        when:
        def res = mvc.perform(get("/post/my")
                .param("pageNo", pageNo)
                .param("pageSize", pageSize)
                .header("x-moyeo-auth-token", jwtString)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)) // deprecate 되었지만, utf8을 사용하지 않으면 한글이 깨짐

        then:
        res.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(cont))
        1 * service.findMyPosts(_, _, _) >> new ResMyPostDto(List.of(), pageNo as int, pageSize as int, false)


        where:
        userId     | pageNo | pageSize
        "fdf11123" | "0"    | "10"
        "xxxxxx"   | "2"    | "5"
        "f11123"   | "100"  | "1"
    }
}
