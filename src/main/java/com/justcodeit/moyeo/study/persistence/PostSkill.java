package com.justcodeit.moyeo.study.persistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "post_skill_tag")
public class PostSkill {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Post post;

  private String skillCode;

  private PostSkill() {
  }

  public PostSkill(String skillCode) {
    this.skillCode = skillCode;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  public String getSkillCode() {
    return this.skillCode;
  }
}
