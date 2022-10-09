package com.justcodeit.moyeo.study.model.skill;

import com.justcodeit.moyeo.study.model.type.SkillCategory;

public class SkillCategoryConverter {
    public static SkillCategory getForFolderName(String skillEngWord) {
        for (SkillCategory skillCategory : SkillCategory.values()) {
            String engWord = skillCategory.getEngWord();
            if(engWord.equals(skillEngWord)) {
                return skillCategory;
            }
        }
        return SkillCategory.ETC;
    }
}
