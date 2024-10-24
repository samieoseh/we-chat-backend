package com.samuel.wechat.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private  String id;
    private String username;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String status;
    private Date createdAt;
    private String role;
}
