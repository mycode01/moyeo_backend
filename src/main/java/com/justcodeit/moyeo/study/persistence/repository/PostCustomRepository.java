package com.justcodeit.moyeo.study.persistence.repository;

import com.justcodeit.moyeo.study.persistence.Post;
import com.justcodeit.moyeo.study.persistence.QPost;
import com.justcodeit.moyeo.study.persistence.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * querydsl 테스트용 레파지토리입니다. 실제로 사용하지 않습니다.
 */
@Repository
public class PostCustomRepository extends QuerydslRepositorySupport {

  private final JPAQueryFactory queryFactory;

  public PostCustomRepository(JPAQueryFactory queryFactory) {
    super(Post.class);
    this.queryFactory = queryFactory;
  }

  /**
   * Querydsl InnerJoin 예제
   */
  public List<Post> getUserPosts() {
    QUser user = QUser.user;
    QPost post = QPost.post;

    // Join시 innerJoin 또는 leftJoin 메서드로 사용 가능.
    return queryFactory
            .selectFrom(post)
            .innerJoin(post.user, user) // XXXJoin(가져올 Target , 별칭[Alias])
            .fetchJoin() // N + 1 문제를 해결하기 위해서 fetchJoin()
            .distinct()
            .fetch();
  }
}