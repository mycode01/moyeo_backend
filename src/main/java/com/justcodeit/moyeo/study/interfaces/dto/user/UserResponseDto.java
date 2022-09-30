package com.justcodeit.moyeo.study.interfaces.dto.user;

import com.justcodeit.moyeo.study.persistence.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String picture;
    private User.Role role;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.picture = user.getPicture();
        this.role = user.getRole();
    }
}
