package com.justcodeit.moyeo.study.interfaces.dto.s3;

import com.justcodeit.moyeo.study.interfaces.dto.BaseResponse;
import com.justcodeit.moyeo.study.interfaces.dto.SuccessRes;
import com.justcodeit.moyeo.study.model.aws.s3.S3Service;
import com.justcodeit.moyeo.study.persistence.Skill;
import com.justcodeit.moyeo.study.persistence.SkillCategory;
import com.justcodeit.moyeo.study.persistence.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;
    private final S3Service s3Service;

    public BaseResponse skillSave() {
        BaseResponse response;
        try {
            List<String> objectFileName = s3Service.getObjectFileName();

            for (String fileLocation : objectFileName) {
                String[] fileFolderAndName = fileLocation.split("/");
                String folder = fileFolderAndName[0];
                String name = fileFolderAndName[1].replaceAll(".svg","");

                SkillCategory[] values = SkillCategory.values();

                for (SkillCategory skillCategory : values) {
                    String engWord = skillCategory.getEngWord();
                    if(engWord.equals(folder)) {
                        String url = "https://moyeo-skillstack.s3.ap-northeast-2.amazonaws.com/" + fileLocation;
                        Skill skill = new Skill(skillCategory, folder , name, url);
                        skillRepository.save(skill);
                    }
                }
            }
            response = new SuccessRes<>(Boolean.TRUE);
        }catch (Exception e) {
            response = new SuccessRes<>(Boolean.FALSE);
        }
        return response;
    }
}
