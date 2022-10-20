package com.justcodeit.moyeo.study.persistence.repository;

import com.justcodeit.moyeo.study.model.post.QQueryPostsProjection_PostSkillProjection;
import com.justcodeit.moyeo.study.model.post.QQueryPostsProjection_ProjectionBuilder;
import com.justcodeit.moyeo.study.model.post.QueryPostsProjection;
import com.justcodeit.moyeo.study.model.post.QueryPostsProjection.PostSkillProjection;
import com.justcodeit.moyeo.study.model.post.QueryPostsProjection.ProjectionBuilder;
import com.justcodeit.moyeo.study.persistence.QPost;
import com.justcodeit.moyeo.study.persistence.QPostSkill;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Slice<QueryPostsProjection> findByKeywordAndSkillCode(String keyword,
      List<String> skillCode, int pageNo, int pageSize) {

    var fetched = fetchPagedPost(keyword, skillCode, pageNo, pageSize);
    var queriedPostId = postIds(fetched);
    var sub = fetchPostSkillByPostIds(queriedPostId);

    return makeSlice(fetched, sub, pageNo, pageSize);
  }

  private List<ProjectionBuilder> fetchPagedPost(String keyword, List<String> skillCode,
      int pageNo, int pageSize) {
    var post = QPost.post;
    return queryFactory
        .select(
            new QQueryPostsProjection_ProjectionBuilder(post.postId, post.state, post.title,
                post.postDate, post.hits)
        )
        .from(post)
        .leftJoin(post.skillTags, QPostSkill.postSkill)
        .where(
            titleLike(keyword),
            skillCodesIn(skillCode)
        )
        .offset(pageNo * pageSize)
        .limit(pageSize + 1)
        .groupBy(post.postId)
        .fetch();
  }
  private List<PostSkillProjection> fetchPostSkillByPostIds(List<String> queriedPostId) {
    return queryFactory
        .select(new QQueryPostsProjection_PostSkillProjection(QPost.post.postId, QPostSkill.postSkill.skillCode))
        .from(QPostSkill.postSkill)
        .where(postIdIn(queriedPostId))
        .fetch();
//        .transform(
//            groupBy(post.postId).as(list(skillTags)) // n+1  발생
//        );
  }

  private Slice<QueryPostsProjection> makeSlice(List<ProjectionBuilder> res,
      List<PostSkillProjection> skills, int pageNo, int pageSize) {

    var skillMap = toMap(skills);
    // List<PostSkill> 은 사용하기 힘들고 퍼포먼스에도 좋지 않으므로 Map<String, PostSkill> 로 변경

    boolean hasNext = false;
    if (res.size() > pageSize) {
      hasNext = true;
      res.remove(pageSize);
    } // 다음 페이지가 있는지 확인하고, 확인용으로 +1 개를 더 가져온 만큼 숫자를 줄여줌

    var r = map(res, skillMap);
    return new SliceImpl<>(r, PageRequest.of(pageNo, pageSize), hasNext);
  }

  /**
   * <b>Post 와 PostSkill 의 조합으로 리턴시킬 타입으로 변환</b>
   * <br/><br/>
   * skillMap 에서 Post가 가진 PostSkill을 List 형태로 가져와 생성한 빌더 객체에 포함시키고 <br/>
   * 리턴 타입인 QueryPostsProjection 으로 변환한다.
   *
   * @param res QueryPostsProjection 의 빌더 객체
   * @param skillMap QueryPostsProjection 에 포함되어야 할 PostSkillProjection 의 Map
   * @return List of QueryPostsProjection
   */
  private List<QueryPostsProjection> map(List<ProjectionBuilder> res,
      Map<String, List<PostSkillProjection>> skillMap) {
    return res.stream().map(e -> {
          var p = skillMap.get(e.getPostId());
          e.skillCodes(p.stream().map(PostSkillProjection::getSkillCode).collect(Collectors.toList()));
          return e.build();
        }
    ).collect(Collectors.toList());
  }

  private BooleanExpression titleLike(String t) {
    if (isBlank(t)) {
      return null;
    }
    return QPost.post.title.contains(t);
  }

  private BooleanExpression skillCodesIn(List<String> s) {
    if (isBlank(s)) {
      return null;
    }
    return QPostSkill.postSkill.skillCode.in(s);
  }

  private BooleanExpression postIdIn(List<String> i) {
    if (isBlank(i)) {
      return null;
    }
    return QPostSkill.postSkill.post.postId.in(i);
  }

  private List<String> postIds(List<ProjectionBuilder> l) {
    return l.stream().map(ProjectionBuilder::getPostId).collect(Collectors.toList());
  }

  private Map<String, List<PostSkillProjection>> toMap(List<PostSkillProjection> l) {
    return l.stream().collect(Collectors.groupingBy(PostSkillProjection::getPostId));
  }


  private boolean isBlank(String t) {
    return t == null || t.isBlank();
  }

  private boolean isBlank(List<String> s) {
    return s == null || s.size() == 0;
  }


}
