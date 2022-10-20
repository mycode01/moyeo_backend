package com.justcodeit.moyeo.study.application;

import com.justcodeit.moyeo.study.application.exception.NotFoundUserException;
import com.justcodeit.moyeo.study.model.user.ReqUpdateUserDto;
import com.justcodeit.moyeo.study.model.user.UserInfoDto;
import com.justcodeit.moyeo.study.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public void updateUser(String userId, ReqUpdateUserDto dto){
    var user = userRepository.findByUserId(userId)
        .orElseThrow(NotFoundUserException::new);

    user.update(dto.getNickname(), dto.getIntroduce(), dto.getSkills());
    userRepository.save(user);
  }

  @Transactional(readOnly = true)
  public UserInfoDto getUserInfo(String userId) {
    var user = userRepository.findByUserId(userId)
        .orElseThrow(NotFoundUserException::new);

    return UserInfoDto.fromEntity(user);
  }
}
