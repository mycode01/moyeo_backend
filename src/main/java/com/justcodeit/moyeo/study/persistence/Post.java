package com.justcodeit.moyeo.study.persistence;

import com.justcodeit.moyeo.study.model.type.GatherType;
import com.justcodeit.moyeo.study.model.type.GroupType;
import com.justcodeit.moyeo.study.model.type.PostState;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "post")
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String postId;

  private String ownerId;

  @Enumerated(EnumType.STRING)
  private PostState state;

  private String title;

  private String content;

  private String contact;

  @Enumerated(EnumType.STRING)
  private GroupType groupType;

  @Enumerated(EnumType.STRING)
  private GatherType gatherType;

  @ElementCollection
  private Map<String, Integer> recruitMember;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private Set<PostSkill> skillTags;

  private LocalDateTime postDate;

  private long hits;

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;


  private Post() {
  }

  public Post(String postId, String ownerId, PostState state, String title, String content,
      String contact, GroupType groupType, GatherType gatherType,
      Map<String, Integer> recruitMember,
      Set<PostSkill> skillTags,
      LocalDateTime postDate, long hits) {
    this.postId = postId;
    this.ownerId = ownerId;
    this.state = state;
    this.title = title;
    this.content = content;
    this.contact = contact;
    this.groupType = groupType;
    this.gatherType = gatherType;
    this.recruitMember = recruitMember;
    this.skillTags = skillTags;
    this.postDate = postDate;
    this.hits = hits;
  }

  public void edit(String title, String content, String contact, GroupType groupType,
      GatherType gatherType, Set<PostSkill> skillTags, Map<String, Integer> member) {
    this.title = title;
    this.content = content;
    this.contact = contact;
    this.groupType = groupType;
    this.gatherType = gatherType;
    this.recruitMember = member;
    setSkill(skillTags); //
  }

  //region operations
  private void setSkill(Set<PostSkill> skillTags) {
    this.skillTags.clear();
//    this.skillTags = skillTags; // hibernate 가 구현하는 방식에 어긋나 문제가 발생함
    this.skillTags.addAll(skillTags);
    this.skillTags.forEach(e -> e.setPost(this));
  }

  public boolean isOwner(String userId) {
    return this.ownerId.equals(userId);
  }

  public void changeState() {
    this.state = this.state.next();
  }

  public void hitsUp(Function<Long, Long> func) {
    this.hits = func.apply(this.hits);
  }

  //endregion

  // region getters
  public String getPostId() {
    return postId;
  }

  public String getOwnerId() {
    return ownerId;
  }

  public PostState getState() {
    return state;
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public String getContact() {
    return contact;
  }

  public GroupType getGroupType() {
    return groupType;
  }

  public GatherType getGatherType() {
    return gatherType;
  }

  public Map<String, Integer> getRecruitMember() {
    return recruitMember;
  }

  public Set<PostSkill> getSkillTags() {
    return skillTags;
  }


  public LocalDateTime getPostDate() {
    return postDate;
  }

  public long getPostDate(ZoneId zoneId) {
    return postDate.atZone(zoneId).toEpochSecond();
  }

  public long getHits() {
    return hits;
  }
  // endregion

}
