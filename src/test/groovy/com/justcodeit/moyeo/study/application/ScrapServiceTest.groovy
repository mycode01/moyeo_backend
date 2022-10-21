package com.justcodeit.moyeo.study.application

import com.justcodeit.moyeo.study.application.exception.AlreadyExistScrapException
import com.justcodeit.moyeo.study.persistence.Scrap
import com.justcodeit.moyeo.study.persistence.repository.ScrapRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.SliceImpl
import spock.lang.Specification

class ScrapServiceTest extends Specification {
    ScrapRepository repo = Mock()
    ScrapService service = new ScrapService(repo)

    def "GetMyScraps"() {
        given:
        def expect = new SliceImpl(List.of(), PageRequest.of(pageNo, pageSize), hasNext)
        when:
        def res = service.getMyScraps(userId, pageNo, pageSize)
        then:
        with(res) {
            res.pageSize == pageSize
            res.pageNo == pageNo
            res.hasNext == hasNext
        }
        1 * repo.findByUserId(_, _) >> expect

        where:
        userId    | pageNo | pageSize | hasNext
        "userId1" | 0      | 20       | false
        "userId2" | 1      | 5        | true
        "userId3" | 4      | 1        | true

    }

    def "when DoScrap successfully executed"() {
        when:
        service.doScrap("userId", "postId")
        then:
        1 * repo.save(_) >> { Scrap scrap ->
            scrap.userId == "userId"
            scrap.postId == "postId"
        }
    }

    def "when DoScrap cause duplicate"() {
        when:
        service.doScrap("userId", "postId")
        then:
        thrown(AlreadyExistScrapException.class)
        1 * repo.save(_) >> { throw new RuntimeException() }
    }

    def "UndoScrap"() {
        when:
        service.undoScrap(userId, postId)
        then:
        1 * repo.deleteByUserIdAndPostId(userId, postId)
        where:
        userId    | postId
        "userId1" | "postId1"
        "userId2" | "postId2"
    }
}
