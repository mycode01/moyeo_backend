package com.justcodeit.moyeo.study.interfaces.resource;

import com.justcodeit.moyeo.study.model.aws.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/s3")
@RestController
@RequiredArgsConstructor
public class S3Controller {
    private final S3Service s3Service;

    @PostMapping
    public void uploadSkill() {
        String path = "C:\\Users\\beetr\\OneDrive\\바탕 화면\\skill stack\\svg";
        s3Service.upload2(path);
    }
}
