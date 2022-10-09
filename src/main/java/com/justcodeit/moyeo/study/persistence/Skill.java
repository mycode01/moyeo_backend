package com.justcodeit.moyeo.study.persistence;

import com.justcodeit.moyeo.study.model.type.SkillCategory;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "skill")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SkillCategory skillCategory;
    private String folderName;
    private String name;
    private String imageUrl;

    public Skill(SkillCategory skillCategory, String folderName, String name, String imageUrl) {
        this.skillCategory = skillCategory;
        this.folderName = folderName;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public Skill update(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
        return this;
    }

    public SkillCategory getSkillCategory() {
        return skillCategory;
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
