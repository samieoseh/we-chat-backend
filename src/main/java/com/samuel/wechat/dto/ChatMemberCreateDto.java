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
public class ChatMemberCreateDto {
    private String id;
    private String chatId;
    private String userId;
    private String role;
    private Date joinedAt;
}
