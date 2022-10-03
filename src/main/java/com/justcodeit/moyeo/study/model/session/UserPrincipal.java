package com.justcodeit.moyeo.study.model.session;

import com.justcodeit.moyeo.study.persistence.User;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Data
@AllArgsConstructor
public class UserPrincipal implements OAuth2User, UserDetails, OidcUser {

  private final String displayName;
  private final String email;
  private final String roleType;
  private final String domesticId;
  private final String providerType;
  private final Collection<GrantedAuthority> authorities;
  private Map<String, Object> attributes;

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getName() {
    return displayName;
  }

  @Override
  public String getUsername() {
    return email;
  }
  // security 에서 username 은 id를 뜻한다

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public Map<String, Object> getClaims() {
    return null;
  }

  @Override
  public OidcUserInfo getUserInfo() {
    return null;
  }

  @Override
  public OidcIdToken getIdToken() {
    return null;
  }

  public static UserPrincipal create(User user) {
    return new UserPrincipal(
        user.getDisplayName(),
        user.getEmail(),
        user.getRoleKey(),
        user.getDomesticId(),
        user.getProviderType(),
        Collections.singletonList(new SimpleGrantedAuthority(user.getRoleKey())),
        null
    );
  }

  public static UserPrincipal create(User user, Map<String, Object> attributes) {
    UserPrincipal userPrincipal = create(user);
    userPrincipal.setAttributes(attributes);

    return userPrincipal;
  }
}