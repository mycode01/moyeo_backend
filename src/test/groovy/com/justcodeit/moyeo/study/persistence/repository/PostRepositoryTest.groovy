package com.justcodeit.moyeo.study.persistence.repository

import com.justcodeit.moyeo.study.common.RandomIdUtil
import com.justcodeit.moyeo.study.config.QuerydslConfig
import com.justcodeit.moyeo.study.model.type.GatherType
import com.justcodeit.moyeo.study.model.type.GroupType
import com.justcodeit.moyeo.study.model.type.PostState
import com.justcodeit.moyeo.study.persistence.Post
import com.justcodeit.moyeo.study.persistence.PostSkill
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.stream.Collectors

@ActiveProfiles("local")
@Import(QuerydslConfig.class)
@DataJpaTest
class PostRepositoryTest extends Specification {
    @Autowired
    PostRepository repo;

    def "when FindByPostId cannot find by postId"() {
        expect:
        repo.findByPostId(postId) == Optional.ofNullable(null)

        where:
        postId << ["notexistid1", "notexistid2", "notexistid3"]
    }

    def "when FindByPostId successfully find by postId"() {
        given:
        def postSkills = Set.<PostSkill> of(new PostSkill("skillcode1"), new PostSkill("skillcode2"))
        def post = new Post(postId, "anyOwnerId", PostState.ON_RECRUITING, "anyTitleString", "any content string",
                "anyContactString", GroupType.BASIC_STUDY, GatherType.OFFLINE, new HashMap<String, Integer>(), postSkills,
                LocalDateTime.now(ZoneId.systemDefault()), 0)
        repo.save(post)

        when:
        def fetched = repo.findByPostId(postId).get()

        then:
        with(fetched) {
            id > 0 // groovy 에서는 private 멤버에도 접근이 가능함
            title == "anyTitleString"
            it.postId == postId
        }


        where:
        postId << ["alwaysExistsId1", "alwaysExistsId2", "alwaysExistsId2"]
    }

    def "when FindByOwnerId cannot find by ownerId"() {
        when:
        def slice = repo.findByOwnerId(ownerId, pr)
        then:
        !slice.hasContent()
        slice.number == pr.getPageNumber()
        slice.size == pr.getPageSize()
        !slice.hasNext()

        where:
        ownerId << ["notexistid1", "notexistid2", "notexistid3"]
        pr << [PageRequest.of(0, 20), PageRequest.of(0, 5), PageRequest.of(0, 1)]
    }

    def "when FindByOwnerId successfully find by ownerId"() {
        given:
        def postIds = getPostIds(n)
        def targets = dummyPost(postIds, ownerId)
        def others = dummyPost(List.of("others1", "others2", "others3", "others4"), "othersid")
        targets.addAll(others)

        repo.saveAll(targets)

        when:
        def slice = repo.findByOwnerId(ownerId, pr)

        then:
        slice.hasContent()
        slice.number == pr.getPageNumber()
        slice.size == pr.getPageSize()
        if (pr.getPageSize() < postIds.size()) {
            slice.hasNext()
        } else {
            !slice.hasNext()
        }
        def p = slice.getContent()
        for (i in 0..<p.size()) {
            def d = p.get(i)
            d.ownerId == ownerId
        }


        where:
        n << [10, 20, 100]
        ownerId << ["ownerId1", "ownerId2", "randomId123123"]
        pr << [PageRequest.of(0, 20), PageRequest.of(0, 5), PageRequest.of(0, 1)]

    }


    def "DeleteByOwnerIdAndPostId"() {
        given:
        def postIds = getPostIds(n)
        def posts = dummyPost(postIds, ownerId)
        def others = dummyPost(List.of("others1", "others2", "others3", "others4"), "othersid")
        posts.addAll(others)

        repo.saveAll(posts)

        def size = repo.findAll().size()
        def random = new Random()

        when:
        def targetPostId = n != 0 ? postIds.get(random.nextInt(n)) : "notexitstId"
        repo.deleteByOwnerIdAndPostId(ownerId, targetPostId)

        then:
        def all = repo.findAll()
        for (i in 0..<all.size()) {
            def p = all.get(i)
            p.postId != targetPostId
        }


        where:
        n << [10, 20, 0]
        ownerId << ["ownerId1", "ownerId2", "randomId123123"]
    }

    def "when findByKeywordAndSkillCode successfully find by keyword"() {
        given:
        def titles = List.of("title1", "title2", "title3", "specify name 123", "title4", "title5")
        def posts = dummyPostWithTitleString(titles)
        repo.saveAll(posts)

        when:
        def res = repo.findByKeywordAndSkillCode("pecif", Collections.emptyList(), 0, 10)

        then:
        with(res.getContent().get(0)) {
            it.title.contains("pecif")
        }
    }

    def "when findByKeywordAndSkillCode successfully find by skillCodes"() {
        given:
        def skillCodes = List.of(List.of("123", "456"), List.of( "456", "789"),
                List.of("abc", "efg"), List.of("123", "abc"))
        def posts = dummyPostWithSkillCodes(skillCodes)
        repo.saveAll(posts)

        when:
        def res = repo.findByKeywordAndSkillCode("", List.of("123", "abc"), 0, 10)

        then:
        res.getContent().size() == 3
        def p = res.getContent()
        for (i in 0..<p.size()) {
            def d = p.get(i)
            d.getSkillCodes().contains("123") || d.getSkillCodes().contains("abc")
        }
    }

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

    def getPostIds(n) {
        def postIdGenerator = new RandomIdUtil()
        def postIds = new ArrayList()
        for (i in 0..<n) {
            postIds.add(postIdGenerator.postId())
        }
        return postIds
    }

    def dummyPostWithTitleString(keywords) {
        def postIds = getPostIds(keywords.size())
        def res = new ArrayList();
        for (i in 0..<postIds.size()) {
            def postId = postIds.get(i)
            def title = keywords.get(i)
            def post = new Post(postId, "ownerId", PostState.ON_RECRUITING, title, "any content string",
                    "anyContactString", GroupType.BASIC_STUDY, GatherType.OFFLINE, new HashMap<String, Integer>(), Set.of(),
                    LocalDateTime.now(ZoneId.systemDefault()), 0)
            res.add(post)
        }
        return res
    }

    def dummyPostWithSkillCodes(List<List<String>> skillCodes) {
        def postIds = getPostIds(skillCodes.size())
        def res = new ArrayList();
        for (i in 0..<postIds.size()) {
            def postId = postIds.get(i)
            def postSkills = skillCodes.get(i).stream().map(PostSkill::new).collect(Collectors.toSet())
            def post = new Post(postId, "ownerId", PostState.ON_RECRUITING, "any title String", "any content string",
                    "anyContactString", GroupType.BASIC_STUDY, GatherType.OFFLINE, new HashMap<String, Integer>(), postSkills,
                    LocalDateTime.now(ZoneId.systemDefault()), 0)
            postSkills.forEach(e->e.setPost(post))
            res.add(post)
        }
        return res
    }
}
