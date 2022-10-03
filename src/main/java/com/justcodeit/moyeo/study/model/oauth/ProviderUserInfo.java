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
  private String domesticId; // provider 내 에서 유니크한 아이디
  private String providerType;


  private ProviderUserInfo() {
  }

  public ProviderUserInfo(Map<String, Object> attributes,
      String nameAttributeKey, String name,
      String email, String picture, String domesticId, String providerType) {
    this.attributes = attributes;
    this.nameAttributeKey = nameAttributeKey;
    this.name = name;
    this.email = email;
    this.picture = picture;
    this.domesticId = domesticId;
    this.providerType = providerType;
  }

  public static ProviderUserInfo of(String registrationId,
      String userNameAttributeName,
      Map<String, Object> attributes) {
    if ("kakao".equals(registrationId)) {
      return ofKakao("id", attributes);
    } else if ("github".equals(registrationId)) {
      return ofGithub("id", attributes);
    }
    return ofGoogle(userNameAttributeName, attributes);
  }

  private static ProviderUserInfo ofGithub(String userNameAttributeName,
      Map<String, Object> attributes) {
    return new ProviderUserInfo(attributes, userNameAttributeName, (String) attributes.get("name"),
        (String) attributes.get("email"), (String) attributes.get("avatar_url"),
        (String) attributes.get(userNameAttributeName), "github");
  }

  private static ProviderUserInfo ofGoogle(String userNameAttributeName,
      Map<String, Object> attributes) {
    return new ProviderUserInfo(attributes, userNameAttributeName, (String) attributes.get("name"),
        (String) attributes.get("email"), (String) attributes.get("picture"),
        (String) attributes.get(userNameAttributeName), "google");
  }

  private static ProviderUserInfo ofKakao(String userNameAttributeName,
      Map<String, Object> attributes) {
    var kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    var profile = (Map<String, Object>) kakaoAccount.get("profile");
    return new ProviderUserInfo(attributes, userNameAttributeName, (String) profile.get("nickname"),
        (String) kakaoAccount.get("email"), (String) profile.get("profile_image_url"),
        String.valueOf((Long) attributes.get(userNameAttributeName)), "kakao");
    //(String)profile.get("profile_image_url") 프로필 이미지가 두가지 (String)profile.get("thumbnail_image_url")
  }
}
