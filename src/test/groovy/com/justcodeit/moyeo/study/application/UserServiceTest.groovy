package com.justcodeit.moyeo.study.application

import com.justcodeit.moyeo.study.application.exception.NotFoundUserException
import com.justcodeit.moyeo.study.model.type.Role
import com.justcodeit.moyeo.study.model.user.ReqUpdateUserDto
import com.justcodeit.moyeo.study.persistence.User
import com.justcodeit.moyeo.study.persistence.repository.UserRepository
import spock.lang.Specification

class UserServiceTest extends Specification {
    UserRepository repo = Mock()
    UserService service = new UserService(repo)

    def "when UpdateUser successfully executed"() {
        given:
        def user = new User(userId, nickname, "email", "pic", Role.USER, "disname", "protype", "di")
        def dto = new ReqUpdateUserDto(newNick, intro, skill)
        when:
        service.updateUser(userId, dto)
        then:
        1 * repo.findByUserId(_) >> Optional.of(user)
        1 * repo.save(_) >> { User u ->
            u.nickname == newNick
            u.introduce == newNick
            u.skillTags == skill
        }
        where:
        userId    | nickname   | newNick       | intro         | skill
        "userId1" | "nickname" | "newnickname" | "new intro"   | Set.of("code1", "code2")
        "userId2" | "noname"   | "newNoname"   | "new intro2 " | Set.of("dddfd", "xxxx")

    }

    def "when UpdateUser cannot find user"() {
        given:
        def dto = new ReqUpdateUserDto(newNick, intro, skill)
        when:
        service.updateUser(userId, dto)
        then:
        thrown(NotFoundUserException.class)
        1 * repo.findByUserId(_) >> Optional.ofNullable(null)
        0 * repo.save()
        where:
        userId    | newNick       | intro         | skill
        "userId1" | "newnickname" | "new intro"   | Set.of("code1", "code2")
        "userId2" | "newNoname"   | "new intro2 " | Set.of("dddfd", "xxxx")
    }

    def "when GetUserInfo successfully find by userId"() {
        given:
        def user = new User(userId, "nickname", "email", "pic", Role.USER, "disname", "protype", "di")

        when:
        def res = service.getUserInfo(userId)
        then:
        res.userId == userId
        1 * repo.findByUserId(_) >> Optional.of(user)

        where:
        userId << ["userid1", "userid2"]
    }

    def "when GetUserInfo cannot find user"() {
        when:
        service.getUserInfo(userId)
        then:
        thrown(NotFoundUserException.class)
        1 * repo.findByUserId(_) >> Optional.ofNullable(null)

        where:
        userId << ["userid1", "userid2"]
    }
}
