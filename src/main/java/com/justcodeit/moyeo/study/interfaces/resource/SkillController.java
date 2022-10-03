package com.justcodeit.moyeo.study.interfaces.resource;

import com.justcodeit.moyeo.study.interfaces.dto.BaseResponse;
import com.justcodeit.moyeo.study.interfaces.dto.s3.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/skill")
@RestController
@RequiredArgsConstructor
public class SkillController {
    private final SkillService skillService;

    @PostMapping
    public BaseResponse uploadZip() {
        return skillService.skillSave();
    }
}
