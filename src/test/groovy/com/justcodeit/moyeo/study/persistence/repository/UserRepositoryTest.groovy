package com.justcodeit.moyeo.study.persistence.repository

import com.justcodeit.moyeo.study.config.QuerydslConfig
import com.justcodeit.moyeo.study.model.type.Role
import com.justcodeit.moyeo.study.persistence.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles("local")
@Import(QuerydslConfig.class)
@DataJpaTest
class UserRepositoryTest extends Specification {
    @Autowired
    UserRepository repo

    def "FindByEmail"() {
        given:
        def other = new User("other1", "nicknamestring", "void@nowhere.com", "http://naver.com", Role.USER, "displayname", "nobody", "12391jdfjdj")
        repo.save(other)
        def user = new User(userId, "nicknamestring", email, "http://naver.com", Role.USER, "displayname", "nobody", "12391jdjdwwj")
        repo.save(user)
        def other2 = new User("other2", "nicknamestring", "void@nowhere.com", "http://naver.com", Role.USER, "displayname", "nobody", "12391fgjdjdj")
        repo.save(other2)

        when:
        def res = repo.findByEmail(email).get()
        then:
        res.userId == userId
        res.email == email

        where:
        userId << ["123123", "tester"]
        email << [ "nobody@nowhere.com", "dddd@google.com"]
    }

    def "FindByDomesticIdAndProviderType"() {
        given:
        def other = new User("other1", "nicknamestring", "void@nowhere.com", "http://naver.com", Role.USER, "displayname", "naver", "12391jdjdj")
        repo.save(other)
        def user = new User("userId1", "nicknamestring", "void@nowhere.com", "http://naver.com", Role.USER, "displayname", pId, di)
        repo.save(user)
        def other2 = new User("other2", "nicknamestring", "void@nowhere.com", "http://naver.com", Role.USER, "displayname", pId, "12391dfdjdjdj")
        repo.save(other2)

        when:
        def res = repo.findByDomesticIdAndProviderType(di, pId).get()
        then:
        res.domesticId == di
        res.providerType == pId

        where:
        pId << ["naver", "google", "github"]
        di << ["naverId123123", "googleId123123", "githubid123123"]
    }

    def "FindByUserId"() {
        given:
        def other = new User("other1", "nicknamestring", "void@nowhere.com", "http://naver.com", Role.USER, "displayname", "nobody", "12391j2ggdjdj")
        repo.save(other)
        def user = new User(userId, "nicknamestring", "void@nowhere.com", "http://naver.com", Role.USER, "displayname", "nobody", "12391j234djdj")
        repo.save(user)
        def other2 = new User("other2", "nicknamestring", "void@nowhere.com", "http://naver.com", Role.USER, "displayname", "nobody", "12391fdjdjdj")
        repo.save(other2)

        when:
        def res = repo.findByUserId(userId).get()

        then:
        res.userId == userId

        where:
        userId << ["testid1", "testid2"]

    }
}
