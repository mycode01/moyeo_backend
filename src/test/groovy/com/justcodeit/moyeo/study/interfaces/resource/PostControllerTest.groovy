package com.justcodeit.moyeo.study.interfaces.resource

import com.fasterxml.jackson.databind.ObjectMapper
import com.justcodeit.moyeo.study.application.PostService
import com.justcodeit.moyeo.study.application.oauth.OAuthUserService
import com.justcodeit.moyeo.study.application.oauth.OAuth2AuthenticationSuccessHandler
import com.justcodeit.moyeo.study.config.ApplicationConfig
import com.justcodeit.moyeo.study.config.SecurityConfig
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ActiveProfiles("local")
@Import([ApplicationConfig.class, SecurityConfig.class])
@WebMvcTest(controllers = [PostController.class])
class PostControllerTest extends Specification {

    @Autowired
    private MockMvc mvc
    @SpringBean
    OAuthUserService oAuthUserService = Mock()
    @SpringBean
    OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler = Mock()
    @SpringBean
    PostService service = Mock()
    @Autowired
    PostController ctrl;
    @Autowired
    private ObjectMapper objectMapper;

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
        "title" | ""                                 | "0"      | "20"
        ""      | "skillcode1,skillcode2,skillcode3" | "0"      | "20"
        ""      | ""                                 | "0"      | "20"
    }

    def "GetPost"() {
        given:
        def expected = new ResPostDetailDto(postId, "", PostState.ON_PROGRESS, "title", "content", "contact", 0, GroupType.BASIC_STUDY, GatherType.OFFLINE, Set.of(), Map.of())

        when:
        def res = mvc.perform(get("/post/"+postId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)) // deprecate 되었지만, utf8을 사용하지 않으면 한글이 깨짐

        then:
        res.andExpect(status().isOk())
        res.andReturn().getResponse().getContentAsString().contains(postId)
        1 * service.findPost(_) >> expected


        where:
        postId << ["postid1","postid3","postid2"]
    }

    def "CreatePost"() {
    }

    def "UpdatePostState"() {
    }

    def "DeletePost"() {
    }

    def "GetMyPost"() {
    }
}
