package com.justcodeit.moyeo.study.persistence.repository;

import com.justcodeit.moyeo.study.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserJPA CRUD 테스트용 레파지토리입니다.
 */
public interface UserJPARepository extends JpaRepository<User,Long> {

}