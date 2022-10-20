package com.justcodeit.moyeo.study.application;

import com.justcodeit.moyeo.study.application.exception.AlreadyExistScrapException;
import com.justcodeit.moyeo.study.model.scrap.ResMyScrapDto;
import com.justcodeit.moyeo.study.persistence.Scrap;
import com.justcodeit.moyeo.study.persistence.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ScrapService {

  private final ScrapRepository scrapRepository;

  @Transactional(readOnly = true)
  public ResMyScrapDto getMyScraps(String userId, int pageNo, int pageSize) {
    var pageable = PageRequest.of(pageNo, pageSize);
    var slice = scrapRepository.findByUserId(userId, pageable);
    return ResMyScrapDto.toModel(slice); //todo post를 보여줘야 한다면?
  }

  @Async
  @Transactional
  public void doScrap(String userId, String postId) {
    saveScrap(userId, postId);
  }


  @Async
  @Transactional
  public void undoScrap(String userId, String postId) {
    scrapRepository.deleteByUserIdAndPostId(userId, postId);
  }


  private void saveScrap(String userId, String postId) {
    try {
      var scrap = new Scrap(userId, postId);
      scrapRepository.save(scrap);
    } catch (RuntimeException e) {
      // persistenceException 이 발생하지만 엄밀히 따지자면
      // service는 persistence를 위한 레이어가 아니기 때문에 상위 exception 캐치
      throw new AlreadyExistScrapException();
    }
  }

}
