package com.justcodeit.moyeo.study.application

import com.justcodeit.moyeo.study.model.skill.StackInfoDto
import com.justcodeit.moyeo.study.model.type.SkillCategory
import com.justcodeit.moyeo.study.persistence.Skill
import com.justcodeit.moyeo.study.persistence.repository.SkillRepository
import spock.lang.Specification

class StackServiceTest extends Specification {
    SkillRepository repo = Mock()
    StackService service = new StackService(repo)

    def "GetStackList"() {
        when:
        def res = service.getStackList()
        then:
        with(res) {
            it.size() == 1
            it.get(0).name == "name"
        }
        1 * repo.findAll() >> List.of(new Skill(SkillCategory.BACK_END, "foldername", "name", "url"))
    }
}
