package com.justcodeit.moyeo.study.persistence;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "scrap",
    indexes = {
        @Index(columnList = "userId"),
        @Index(columnList = "userId, postId", unique = true)
    })
public class Scrap {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String userId;

  private String postId;


  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createdAt;

  private Scrap() {
  }

  public Scrap(String userId, String postId) {
    this.userId = userId;
    this.postId = postId;
  }

  public String getUserId() {
    return userId;
  }

  public String getPostId() {
    return postId;
  }
}
