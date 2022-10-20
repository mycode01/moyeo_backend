package com.justcodeit.moyeo.study.application;


import com.justcodeit.moyeo.study.model.skill.StackInfoDto;
import com.justcodeit.moyeo.study.persistence.repository.SkillRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StackService {
  private final SkillRepository skillRepository;

  @Transactional(readOnly = true)
  public List<StackInfoDto> getStackList(){
    var stackList = skillRepository.findAll();
    return stackList.stream().map(StackInfoDto::fromEntity).collect(Collectors.toList());
  }
}
