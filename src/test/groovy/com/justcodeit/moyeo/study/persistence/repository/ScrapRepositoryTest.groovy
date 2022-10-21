package com.justcodeit.moyeo.study.persistence.repository

import com.justcodeit.moyeo.study.config.QuerydslConfig
import com.justcodeit.moyeo.study.persistence.Scrap
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles("local")
@Import(QuerydslConfig.class)
@DataJpaTest
class ScrapRepositoryTest extends Specification {
    @Autowired
    ScrapRepository repo;

    def "when FindByUserId nothing to find"() {
        expect:
        def slice = repo.findByUserId(userId, pr)
        !slice.hasContent()
        slice.number == pr.getPageNumber()
        slice.size == pr.getPageSize()
        !slice.hasNext()

        where:
        userId << ["notexists", "123123"]
        pr << [PageRequest.of(0, 20), PageRequest.of(0, 100)]
    }

    def "when FindByUserId successfully find by userId"() {
        given:
        def postId = "postId"
        for (i in 0..<n) {
            def others = new Scrap("dummyId1", postId + i)
            repo.save(others)
            def scrap = new Scrap(userId, postId + i)
            repo.save(scrap)
            def others2 = new Scrap("dummyId2", postId + i)
            repo.save(others2)
        }

        when:
        def slice = repo.findByUserId(userId, PageRequest.of(0, sz))

        then:
        slice.getContent().size() == sz > n ? n : sz
        n > sz ? slice.hasNext() : !slice.hasNext()
        for (i in 0..<slice.getContent().size()) {
            slice.getContent().get(i).getUserId() == userId
        }

        where:
        n << [1, 20, 5]
        sz << [20, 5, 1]
        userId << ["useridstring", "1231231241421241231", "useridstring" * 10]
    }

    def "DeleteByUserIdAndPostId"() {
        given:
        def postId = "postId"
        for (i in 0..<n) {
            def others = new Scrap("dummyId1", postId + i)
            repo.save(others)
            def scrap = new Scrap(userId, postId + i)
            repo.save(scrap)
            def others2 = new Scrap("dummyId2", postId + i)
            repo.save(others2)
        }
        def size = repo.findAll().size()
        def random = new Random()


        when:
        def target = n != 0? postId + random.nextInt(n) : "notExistid"
        repo.deleteByUserIdAndPostId(userId, target)

        then:
        n == 0 ? true : repo.findAll().size() < size
        !repo.findAll().stream().anyMatch(e -> e.userId == userId && e.postId == target)

        where:
        n << [10, 1, 0]
        userId << ["userId123", "tester", "toaster"]
    }
}
