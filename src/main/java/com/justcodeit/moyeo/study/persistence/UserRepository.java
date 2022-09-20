package com.justcodeit.moyeo.study.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * querydsl 테스트용 레파지토리입니다.
 * 실제로 사용하지 않을 가능성이 높습니다.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);
}
