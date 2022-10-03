package com.justcodeit.moyeo.study.model;

import com.justcodeit.moyeo.study.model.oauth.ProviderUserInfo;
import com.justcodeit.moyeo.study.persistence.User;
import com.justcodeit.moyeo.study.persistence.User.Role;
import org.springframework.stereotype.Component;

@Component
public class OAuthInfoToUserConverter implements ModelConverter<ProviderUserInfo, User> {

  @Override
  public User convert(ProviderUserInfo s) {
    return new User(s.getEmail(), s.getPicture(), Role.USER, s.getName(), s.getProviderType(),
        s.getDomesticId());
  }
}
