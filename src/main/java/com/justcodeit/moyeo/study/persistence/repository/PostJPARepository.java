package com.justcodeit.moyeo.study.persistence.repository;

import com.justcodeit.moyeo.study.persistence.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CRUD를 쉽게 사용하기 위해서 JPA Repository 사용
 */
public interface PostJPARepository extends JpaRepository<Post,Long> {

}