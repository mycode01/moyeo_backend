package com.justcodeit.moyeo.study.persistence.response;

import com.justcodeit.moyeo.study.persistence.Post;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostUserResponseDto {
    private Long id;
    private String title;
    private String content;

    private UserResponseDto userResponseDto;

    public PostUserResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.userResponseDto = new UserResponseDto(post.getUser());
    }
}
