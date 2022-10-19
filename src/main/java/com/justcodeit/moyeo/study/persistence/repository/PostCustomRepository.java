package com.justcodeit.moyeo.study.persistence.repository;

import com.justcodeit.moyeo.study.model.post.QueryPostsProjection;
import java.util.List;
import org.springframework.data.domain.Slice;

public interface PostCustomRepository {

  Slice<QueryPostsProjection> findByKeywordAndSkillCode(String keyword, List<String> skillCode, int pageNo, int pageSize);

}
