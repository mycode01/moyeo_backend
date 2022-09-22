package com.justcodeit.moyeo.study.model.oauth;

import java.util.Map;
import lombok.Data;

@Data
public class ProviderUserInfo {

  private Map<String, Object> attributes;
  private String nameAttributeKey;
  private String name;
  private String email;
  private String picture;

  private ProviderUserInfo() {
  }

  public ProviderUserInfo(Map<String, Object> attributes,
      String nameAttributeKey, String name,
      String email, String picture) {
    this.attributes = attributes;
    this.nameAttributeKey = nameAttributeKey;
    this.name = name;
    this.email = email;
    this.picture = picture;
  }

  public static ProviderUserInfo of(String registrationId,
      String userNameAttributeName,
      Map<String, Object> attributes) {
    if ("naver".equals(registrationId)) {
      return ofNaver("id", attributes);
    } else if ("kakao".equals(registrationId)) {
      return ofKakao("id", attributes);
    }
    return ofGoogle(userNameAttributeName, attributes);
  }

  private static ProviderUserInfo ofGoogle(String userNameAttributeName,
      Map<String, Object> attributes) {
    return new ProviderUserInfo(attributes, userNameAttributeName, (String) attributes.get("name"),
        (String) attributes.get("email"), (String) attributes.get("picture"));
  }

  private static ProviderUserInfo ofNaver(String userNameAttributeName,
      Map<String, Object> attributes) {
    var response = (Map<String, Object>) attributes.get("response");
    return new ProviderUserInfo(response, userNameAttributeName, (String) response.get("name"),
        (String) attributes.get("email"), (String) attributes.get("picture"));
  }

  private static ProviderUserInfo ofKakao(String userNameAttributeName,
      Map<String, Object> attributes) {
    var kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    var profile = (Map<String, Object>) kakaoAccount.get("profile");
    return new ProviderUserInfo(attributes, userNameAttributeName, (String) profile.get("nickname"),
        (String) kakaoAccount.get("email"), (String) profile.get("profile_image_url"));
    //(String)profile.get("profile_image_url") 프로필 이미지가 두가지 (String)profile.get("thumbnail_image_url")
  }
}
