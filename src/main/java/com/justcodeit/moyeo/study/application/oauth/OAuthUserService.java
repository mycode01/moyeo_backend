package com.justcodeit.moyeo.study.application.oauth;

import com.justcodeit.moyeo.study.common.UserIdGenerator;
import com.justcodeit.moyeo.study.model.ModelConverter;
import com.justcodeit.moyeo.study.model.oauth.ProviderUserInfo;
import com.justcodeit.moyeo.study.model.session.UserPrincipal;
import com.justcodeit.moyeo.study.persistence.User;
import com.justcodeit.moyeo.study.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuthUserService implements OAuth2UserService {

  private final DefaultOAuth2UserService defaultOAuth2UserService;
  private final UserRepository userRepository;
  private final ModelConverter<ProviderUserInfo, User> userConverter;
  private final UserIdGenerator userIdGenerator;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    log.debug(userRequest.toString());

    var oAuth2User = defaultOAuth2UserService.loadUser(userRequest);

    String providerId = userRequest.getClientRegistration().getRegistrationId();
    String userNameAttributeName = userRequest.getClientRegistration()
        .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
    log.debug(oAuth2User.getName());

    ProviderUserInfo info = ProviderUserInfo.of(providerId, userNameAttributeName, userIdGenerator.userId(),
        oAuth2User.getAttributes());
    var user = saveOrUpdate(info);

    return UserPrincipal.create(user, oAuth2User.getAttributes());
  }

  private User saveOrUpdate(ProviderUserInfo info) {
    var user = userRepository.findByDomesticIdAndProviderType(
            info.getDomesticId(), info.getProviderType())
        .map(e -> e.update(info.getName(), info.getPicture()))
        .orElse(userConverter.convert(info));
    return userRepository.save(user);
  } //github 은 email이 변경이 가능한 서비스이기 때문에 업데이트를 해주는게 맞는지...
}
