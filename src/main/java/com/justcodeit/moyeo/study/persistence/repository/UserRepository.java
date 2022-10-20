package com.justcodeit.moyeo.study.persistence.repository;

import com.justcodeit.moyeo.study.persistence.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


  Optional<User> findByEmail(String email);

  Optional<User> findByDomesticIdAndProviderType(String domesticId, String providerType);

  Optional<User> findByUserId(String userId);
}