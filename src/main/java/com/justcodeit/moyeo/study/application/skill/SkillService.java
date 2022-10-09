package com.justcodeit.moyeo.study.application.skill;

import com.justcodeit.moyeo.study.application.aws.s3.S3Service;
import com.justcodeit.moyeo.study.model.skill.SkillCategoryConverter;
import com.justcodeit.moyeo.study.model.type.SkillCategory;
import com.justcodeit.moyeo.study.persistence.Skill;
import com.justcodeit.moyeo.study.persistence.repository.SkillRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;
    private final S3Service s3Service;

    public Boolean skillSaveAllFromS3() {
        try {
            List<String> objectFileName = s3Service.getObjectFileName();

            for (String fileLocation : objectFileName) {
                String[] fileFolderAndName = fileLocation.split("/");
                String folder = fileFolderAndName[0];
                String name = fileFolderAndName[1].replaceAll(".svg","");

                SkillCategory skillCategory = SkillCategoryConverter.getForFolderName(folder);
                String url = "" + skillCategory.getEngWord();

                Skill skill = new Skill(skillCategory, folder , name, url);
                skillRepository.save(skill);
            }
        }catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
