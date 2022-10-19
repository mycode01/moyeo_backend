package com.justcodeit.moyeo.study.persistence.repository;

import com.justcodeit.moyeo.study.model.post.QQueryPostsProjection;
import com.justcodeit.moyeo.study.model.post.QueryPostsProjection;
import com.justcodeit.moyeo.study.persistence.QPost;
import com.justcodeit.moyeo.study.persistence.QPostSkill;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Slice<QueryPostsProjection> findByKeywordAndSkillCode(String keyword, List<String> skillCode, int pageNo, int pageSize) {
    var post = QPost.post;
    var skillTags = QPostSkill.postSkill;
    var fetched = queryFactory
        .select(new QQueryPostsProjection(post.postId, post.state, post.title, post.postDate, post.hits))
        .from(post)
        .leftJoin(post.skillTags, skillTags)
        .where(post.title.contains(keyword)
            .and(
                skillTags.skillCode.in(skillCode)
            ))
        .offset(pageNo)
        .limit(pageSize)
        .fetch();
    return makeSlice(fetched, pageNo, pageSize);
  }

  private Slice<QueryPostsProjection> makeSlice(List<QueryPostsProjection> res, int pageNo, int pageSize){
    boolean hasNext = false;
    if(res.size() > pageSize) {
      hasNext = true;
      res.remove(pageSize);
    }
    return new SliceImpl<>(res, PageRequest.of(pageNo, pageSize), hasNext);

  }


}
