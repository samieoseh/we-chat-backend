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
public class ChatMemberReadDto {
    private String id;
    private UserDto user;
    private String role;
    private Date joinedAt;
}
