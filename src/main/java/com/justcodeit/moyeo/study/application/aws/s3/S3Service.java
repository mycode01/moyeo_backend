package com.justcodeit.moyeo.study.application.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.TransferManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    private final TransferManager transferManager;

    @Value("${s3EndPoint}")
    private final String S3_ENDPOINT;

    /**
     * S3에 한개의 파일 업로드 하는 코드
     * 단일 파일 업로드하기 위해서 사용.
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public String fileUpload(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getInputStream().available());

        amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), objectMetadata);
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    /**
     * S3에 로컬 폴더를 업로드 하는 코드.
     * S3의 폴더 구조를 로컬 C 드라이브의 폴더에 맞추기 위해서 virtualDirectoryKeyPrefix를 ""로 설정
     */
    public void directoryUpload(String path) {
        File file = new File(path);
        transferManager.uploadDirectory(bucket, path, file, true);
    }

    /**
     * S3에 저장된 파일 객체목록 중 파일의 S3에서의 절대경로를 가져오는 코드
     * @return s3FileLocationList
     */
    public List<String> getObjectFileName() {
        List<String> s3FileLocationList = new ArrayList<>();
        ListObjectsV2Result listObjectsV2Result = amazonS3.listObjectsV2(bucket);

        for (S3ObjectSummary s3ObjectSummary : listObjectsV2Result.getObjectSummaries()) {
            String s3FileLocation = s3ObjectSummary.getKey();
            s3FileLocationList.add(s3FileLocation);
        }
        return s3FileLocationList;
    }
}
