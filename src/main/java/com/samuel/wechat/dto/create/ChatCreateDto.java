package com.samuel.wechat.dto.create;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatCreateDto {
    private String id;
    private String chatName;
    private Boolean isGroup;
    private String createdBy;
    private Date createdAt;
}
