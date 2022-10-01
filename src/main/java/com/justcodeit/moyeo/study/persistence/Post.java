package com.justcodeit.moyeo.study.persistence;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * querydsl 테스트용 엔티티입니다.
 * 실제로 사용하지 않을 가능성이 높습니다.
 */

@Entity
@Table(name = "post")
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  private String content;


  private Post() {
  }

  public Post(String title, String content) {
    this.title = title;
    this.content = content;
  }
}
