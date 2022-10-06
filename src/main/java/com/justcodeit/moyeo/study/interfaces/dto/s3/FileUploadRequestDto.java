package com.justcodeit.moyeo.study.interfaces.dto.s3;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Getter
@Setter
public class FileUploadRequestDto {
    private MultipartFile multipartFile;
    private String directoryName;
}
