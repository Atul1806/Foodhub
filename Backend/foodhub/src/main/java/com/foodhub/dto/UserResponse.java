package com.foodhub.dto;

import com.foodhub.entity.UserRole;
import com.foodhub.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private UserRole role;
    private UserStatus status;

}