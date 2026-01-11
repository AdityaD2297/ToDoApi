package com.application.todoapi.common.response;

import com.application.todoapi.entity.User;

public record UserResponse(
    Long id,
    String username,
    String email,
    User.Role role
) {
    public static UserResponse fromUser(User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole()
        );
    }
}
