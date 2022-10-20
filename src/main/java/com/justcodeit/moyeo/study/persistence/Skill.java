package com.justcodeit.moyeo.study.persistence;

import com.justcodeit.moyeo.study.model.type.SkillCategory;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "skill")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SkillCategory category;

    private String skillCode;
    private String folderName;
    private String name;
    private String imageUrl;

    private Skill(){}

    public Skill(SkillCategory skillCategory, String folderName, String name, String imageUrl) {
        this.category = skillCategory;
        this.folderName = folderName;
        this.name = name;
        this.imageUrl = imageUrl;
    }


    public SkillCategory getCategory() {
        return category;
    }

    public String getSkillCode() {
        return skillCode;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
