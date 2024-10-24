package com.samuel.wechat.dto.read;

import java.util.Date;

import com.samuel.wechat.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatReadDto {
    private String id;
    private String chatName;
    private Boolean isGroup;
    private UserDto createdBy;
    private Date createdAt;
}
