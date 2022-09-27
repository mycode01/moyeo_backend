package com.justcodeit.moyeo.study.persistence.repository;

import com.justcodeit.moyeo.study.persistence.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import com.justcodeit.moyeo.study.persistence.QUser;

import java.util.List;

/**
 * querydsl 테스트용 레파지토리입니다.
 * 실제로 사용하지 않습니다.
 */
@Repository
public class CustomUserRepository extends QuerydslRepositorySupport {
  private final JPAQueryFactory queryFactory;

  public CustomUserRepository(JPAQueryFactory queryFactory) {
    super(User.class);
    this.queryFactory = queryFactory;
  }

  /**
   * 이름으로 사용자 찾는 Querydsl 예제
   */
  public List<User> findByName(String name) {
    QUser user = QUser.user;
    return queryFactory
            .selectFrom(user)
            .where(user.username.eq(name))
            .fetch();
  }
  /**
   * email로 사용자 찾는 예제
   * fetchFirst를 사용해서 한개만 가져옴.
   */
  public User findByEmail(String email) {
    QUser user = QUser.user;

    return queryFactory
            .select(user)
            .from(user)
            .where(user.username.eq(email))
            .fetchFirst();
  }
}