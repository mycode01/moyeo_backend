package com.justcodeit.moyeo.study.application

import com.justcodeit.moyeo.study.application.exception.NotFoundPostException
import com.justcodeit.moyeo.study.common.PostIdGenerator
import com.justcodeit.moyeo.study.model.post.QueryPostsProjection
import com.justcodeit.moyeo.study.model.post.ReqCreatePostDto
import com.justcodeit.moyeo.study.model.post.ReqUpdatePostDto
import com.justcodeit.moyeo.study.model.type.GatherType
import com.justcodeit.moyeo.study.model.type.GroupType
import com.justcodeit.moyeo.study.model.type.PostState
import com.justcodeit.moyeo.study.persistence.Post
import com.justcodeit.moyeo.study.persistence.repository.PostRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.SliceImpl
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.ZoneId

class PostServiceTest extends Specification {
    PostRepository repo = Mock()
    PostIdGenerator idGenerator = Mock()
    PostService service = new PostService(repo, idGenerator)

    def "CreatePost"() {
        given:
        idGenerator.postId() >> postId
        repo.save(_) >> {}
        def dto = new ReqCreatePostDto("userId", "title", "desc", "contact", GroupType.BASIC_STUDY, GatherType.OFFLINE, List.of(), Map.of())
        when:
        def res = service.createPost(dto)
        then:
        res == postId

        where:
        postId << ["randompostId1", "postId2s"]
    }

    def "when UpdatePost successfully function"() {
        given:
        def mockPost = Mock(Post)
        mockPost.isOwner(_) >> true
        mockPost.edit(_, _, _, _, _, _, _) >> {}
        mockPost.getPostId() >> postId
        repo.findByPostId(_) >> Optional.of(mockPost)
        repo.save(_) >> {}
        def dto = new ReqUpdatePostDto(postId, "userId", "title", "desc", "contact", GroupType.BASIC_STUDY, GatherType.OFFLINE, List.of(), Map.of())
        when:
        def res = service.updatePost(dto)
        then:
        res == postId

        where:
        postId << ["randompostId1", "postId2s"]
    }

    def "when UpdatePost cannot find update target post"() {
        given:
        def dto = new ReqUpdatePostDto(postId, "userId", "title", "desc", "contact", GroupType.BASIC_STUDY, GatherType.OFFLINE, List.of(), Map.of())
        when:
        service.updatePost(dto)
        then:
        thrown(NotFoundPostException.class)
        1 * repo.findByPostId(_) >> Optional.ofNullable(null)
        0 * repo.save(_)

        where:
        postId << ["randompostId1", "postId2s"]
    }

    def "when UpdatePost found target post but owner does not match"() {
        given:
        def dto = new ReqUpdatePostDto(postId, "userId", "title", "desc", "contact", GroupType.BASIC_STUDY, GatherType.OFFLINE, List.of(), Map.of())
        def mockPost = Mock(Post)
        mockPost.isOwner(_) >> false
        when:
        service.updatePost(dto)
        then:
        thrown(NotFoundPostException.class)
        1 * repo.findByPostId(_) >> Optional.ofNullable(mockPost)
        0 * repo.save(_)

        where:
        postId << ["randompostId1", "postId2s"]
    }

    def "when UpdateState successfully function"() {
        given:
        def post = new Post(postId, userId, PostState.ON_RECRUITING, "title", "content", "contact", GroupType.BASIC_STUDY, GatherType.OFFLINE, Map.of(), Set.of(), LocalDateTime.now(ZoneId.systemDefault()), 0)
        repo.findByPostId(_) >> Optional.of(post)
        when:
        def res = service.updateState(userId, postId)
        then:
        res == postId
        1 * repo.save(_) >> {}

        where:
        userId    | postId
        "userId1" | "postId1"
        "userId2" | "postId2"
    }

    def "when UpdateState cannot find update target post"() {
        given:
        when:
        service.updateState(userId, postId)
        then:
        thrown(NotFoundPostException.class)
        1 * repo.findByPostId(_) >> Optional.ofNullable(null)
        0 * repo.save(_) >> {}

        where:
        userId    | postId
        "userId1" | "postId1"
        "userId2" | "postId2"
    }

    def "when UpdateState found target post but owner does not match"() {
        given:
        def mockPost = Mock(Post)
        mockPost.isOwner(_) >> false
        when:
        service.updateState(userId, postId)
        then:
        thrown(NotFoundPostException.class)
        1 * repo.findByPostId(_) >> Optional.of(mockPost)
        0 * repo.save(_) >> {}

        where:
        userId    | postId
        "userId1" | "postId1"
        "userId2" | "postId2"
    }

    def "Delete"() {
        when:
        service.delete("userId", "postId")

        then:
        1 * repo.deleteByOwnerIdAndPostId(_, _) >> {}

    }

    def "FindMyPosts"() {
        given:
        def pr = PageRequest.of(pageNo, pageSize)
        def expected = new SliceImpl(dummyPost(List.of("postId1", "postId1"), userId), pr, hasNext)
        repo.findByOwnerId(_, _) >> expected
        when:
        def res = service.findMyPosts(userId, pageNo, pageSize)
        then:
        with(res) {
            it.data.size() == 2
            it.pageNo == pageNo
            it.pageSize == pageSize
            it.hasNext == hasNext
        }

        where:
        userId    | pageNo | pageSize | hasNext
        "userId1" | 0      | 20       | false
        "userId2" | 2      | 10       | true
        "userId2" | 100    | 5        | true

    }

    def "FindPosts"() {
        given:
        def rand = new Random()
        def expect = dummySearchProjection(rand.nextInt(pageSize))
        def pr = PageRequest.of(pageNo, pageSize)
        def expectSlice = new SliceImpl(expect, pr, hasNext)
        when:
        def res = service.findPosts("keyword", List.of(), pageNo, pageSize)
        then:
        with(res) {
            it.hasNext == hasNext
            it.pageNo == pageNo
            it.pageSize == pageSize
            it.data.size() <= pageSize
        }
        1 * repo.findByKeywordAndSkillCode(_, _, pageNo, pageSize) >> expectSlice

        where:
        pageNo << [0, 1, 5]
        pageSize << [10, 20, 1]
        hasNext << [true, false, true]
    }

    def "FindPost"() {
        given:
        def post = new Post(postId, "ownerId", PostState.ON_RECRUITING, "anyTitleString", "any content string",
                "anyContactString", GroupType.BASIC_STUDY, GatherType.OFFLINE, new HashMap<String, Integer>(), Set.of(),
                LocalDateTime.now(ZoneId.systemDefault()), 0)
        def mockPost = Spy(post)
        when:
        def res = service.findPost(postId)
        then:
        with(res) {
            it.getPostId() == postId
            it.getOwnerId() == ownerId
            it.hits > 0
        }
        1 * repo.findByPostId(_) >> Optional.of(mockPost)
        1 * mockPost.hitsUp(_)
        1 * repo.save(_) >> {}
        where:
        postId << ["postId1", "postId2"]
    } // 사실 올바르진 않은 테스트, method 안은 블랙박스여야 한다.


    def dummyPost(postIds, ownerId) {
        def res = new ArrayList();
        for (i in 0..<postIds.size()) {
            def postId = postIds.get(i)
            def post = new Post(postId, ownerId, PostState.ON_RECRUITING, "anyTitleString", "any content string",
                    "anyContactString", GroupType.BASIC_STUDY, GatherType.OFFLINE, new HashMap<String, Integer>(), Set.of(),
                    LocalDateTime.now(ZoneId.systemDefault()), 0)
            res.add(post)
        }

        return res
    }

    def dummySearchProjection(n) {
        def res = new ArrayList()
        for (i in 0..<n) {
            res.add(
                    new QueryPostsProjection("postId" + i, PostState.ON_PROGRESS, "title", List.of(), LocalDateTime.now(ZoneId.systemDefault()), 0)
            )
        }
        return res
    }
}
