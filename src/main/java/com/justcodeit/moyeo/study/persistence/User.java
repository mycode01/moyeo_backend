package com.justcodeit.moyeo.study.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * querydsl 테스트용 엔티티입니다.
 * 실제로 사용하지 않을 가능성이 높습니다.
 */
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  @Column(unique = true)
  private String email;
  private String picture;
  @Enumerated(EnumType.STRING)
  private Role role;


  private User(){} //for jpa reflection
  public User(String username, String email, String picture, Role role) {
    this.username = username;
    this.email = email;
    this.picture = picture;
    this.role = role;
  }

  public User update(String username, String picture) {
    this.username = username;
    this.picture = picture;
    return this;
  }

  public String getRoleKey(){
    return this.role.getKey();
  }

  @RequiredArgsConstructor
  @Getter
  public enum Role {
    USER("ROLE_USER", "일반사용자"),
    GUEST("GUEST_USER", "게스트");
    private final String key;
    private final String title;
  }

}
