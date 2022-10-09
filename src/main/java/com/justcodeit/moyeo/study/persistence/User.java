package com.justcodeit.moyeo.study.persistence;

import com.justcodeit.moyeo.study.model.type.Role;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity

@Table(name = "users", // user가 예약어
    indexes = {
        @Index(columnList = "email"),
        @Index(columnList = "domesticId, providerType", unique = true)
    }
)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String email;
  private String picture;
  @Enumerated(EnumType.STRING)
  private Role role;

  private String displayName; // oauth provider 가 전달하는 이름(혹은 해당 서비스에서의 닉네임), moyeo 내의 닉네임이랑은 다름
  private String providerType;
  private String domesticId; // provider가 가지고 있는 유저 구분값

  private User() {
  }

  public User(String email, String picture, Role role, String displayName, String providerType,
      String domesticId) {
    this.email = email;
    this.picture = picture;
    this.role = role;
    this.displayName = displayName;
    this.providerType = providerType;
    this.domesticId = domesticId;
  }

  public User update(String displayName, String picture) {
    this.displayName = displayName;
    this.picture = picture;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public String getDisplayName() {
    return this.displayName;
  }

  public String getRoleKey() {
    return this.role.getKey();
  }

  public String getProviderType() {
    return providerType;
  }

  public String getDomesticId() {
    return domesticId;
  }

}
