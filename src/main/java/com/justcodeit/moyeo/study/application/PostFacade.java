package com.justcodeit.moyeo.study.application;

import com.justcodeit.moyeo.study.application.exception.NotFoundPostException;
import com.justcodeit.moyeo.study.common.PostIdGenerator;
import com.justcodeit.moyeo.study.model.post.ReqCreatePostDto;
import com.justcodeit.moyeo.study.model.post.ResMyPostDto;
import com.justcodeit.moyeo.study.model.post.ResPostDetailDto;
import com.justcodeit.moyeo.study.model.post.ResPostListDto;
import com.justcodeit.moyeo.study.persistence.Post;
import com.justcodeit.moyeo.study.persistence.repository.PostRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostFacade {

  private final PostRepository postRepository;
  private final PostIdGenerator idGenerator;

  @Transactional
  public String createPost(ReqCreatePostDto dto) {
    var post = dto.toEntity(idGenerator, LocalDateTime.now(), 0);
    postRepository.save(post);
    return post.getPostId();
  }

  @Transactional
  public String updateState(String userId, String postId) {
    var post = findUsersPost(postId, userId);

    post.changeState();
    postRepository.save(post);

    return post.getPostId();
  }

  @Transactional
  public void delete(String userId, String postId) {
    var post = findUsersPost(postId, userId);
    postRepository.delete(post);
  }

  public ResMyPostDto findMyPosts(String userId, int pageNo, int pageSize) {
    return findUserPosts(userId, pageNo, pageSize);
  }

  public ResPostListDto findPosts(String keyword, List<String> skillCodes,
      int pageNo, int pageSize) {
    return ResPostListDto.toModel(
        postRepository.findByKeywordAndSkillCode(keyword, skillCodes, pageNo, pageSize));
  }

  public ResPostDetailDto findPost(String postId) {
    var post = postRepository.findByPostId(postId).orElseThrow(NotFoundPostException::new);
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

}
