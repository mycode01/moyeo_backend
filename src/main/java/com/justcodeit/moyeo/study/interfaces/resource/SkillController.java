package com.justcodeit.moyeo.study.interfaces.resource;

import com.justcodeit.moyeo.study.application.skill.SkillService;
import com.justcodeit.moyeo.study.interfaces.dto.SuccessRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/skill")
@RestController
@RequiredArgsConstructor
public class SkillController {
    private final SkillService skillService;

    @PostMapping
    public ResponseEntity<SuccessRes<Boolean>> uploadZip() {
        return ResponseEntity.ok(new SuccessRes<>(skillService.skillSaveAllFromS3()));
    }
}
