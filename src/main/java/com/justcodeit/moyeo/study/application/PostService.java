package com.justcodeit.moyeo.study.application;

import com.justcodeit.moyeo.study.application.exception.NotFoundPostException;
import com.justcodeit.moyeo.study.common.PostIdGenerator;
import com.justcodeit.moyeo.study.model.post.ReqCreatePostDto;
import com.justcodeit.moyeo.study.model.post.ReqUpdatePostDto;
import com.justcodeit.moyeo.study.model.post.ResMyPostDto;
import com.justcodeit.moyeo.study.model.post.ResPostDetailDto;
import com.justcodeit.moyeo.study.model.post.ResPostListDto;
import com.justcodeit.moyeo.study.persistence.Post;
import com.justcodeit.moyeo.study.persistence.repository.PostRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

  private final PostRepository postRepository;
  private final PostIdGenerator idGenerator;

  @Transactional
  public String createPost(ReqCreatePostDto dto) {
    var postId = idGenerator.postId();
    var post = dto.toEntity(postId, LocalDateTime.now(), 0);
    postRepository.save(post);
    return post.getPostId();
  } // todo need refactor

  @Transactional
  public String updatePost(ReqUpdatePostDto dto) {
    var post = findUsersPost(dto.getPostId(), dto.getUserId());
    var updated = buildUpdate(post, dto);
    postRepository.save(updated);
    return updated.getPostId();
  } // todo need refactor

  @Transactional
  public String updateState(String userId, String postId) {
    var post = findUsersPost(postId, userId);

    post.changeState();
    postRepository.save(post);

    return post.getPostId();
  }

  @Async
  @Transactional
  public void delete(String userId, String postId) {
    postRepository.deleteByOwnerIdAndPostId(userId, postId);
  }

  @Transactional(readOnly = true)
  public ResMyPostDto findMyPosts(String userId, int pageNo, int pageSize) {
    return findUserPosts(userId, pageNo, pageSize);
  }

  @Transactional(readOnly = true)
  public ResPostListDto findPosts(String keyword, List<String> skillCodes,
      int pageNo, int pageSize) {
    return ResPostListDto.toModel(
        postRepository.findByKeywordAndSkillCode(keyword, skillCodes, pageNo, pageSize));
  }

  @Transactional
  public ResPostDetailDto findPost(String postId) {
    var post = postRepository.findByPostId(postId).orElseThrow(NotFoundPostException::new);
    post.hitsUp(standardHitsUp);
    postRepository.save(post);
    return ResPostDetailDto.fromEntity(post);
  }

  // privates below

  private Post findUsersPost(String postId, String userId) {
    var post = postRepository.findByPostId(postId)
        .orElseThrow(NotFoundPostException::new);

    if (!post.isOwner(userId)) {
      throw new NotFoundPostException();
    }
    return post;
  }

  private ResMyPostDto findUserPosts(String userId, int pageNo, int pageSize) {
    return ResMyPostDto.toModel(
        postRepository.findByOwnerId(userId, PageRequest.of(pageNo, pageSize)));
  }

  private Post buildUpdate(Post p, ReqUpdatePostDto d) {
    var skills = d.skills();
    p.edit(d.getTitle(), d.getDescribe(), d.getContact(), d.getGroupType(), d.getGatherType(),
        skills, d.getMembers());
    return p;
  }

  private Function<Long, Long> standardHitsUp = l -> ++l;

}
