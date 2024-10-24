package com.samuel.wechat.mappers.impl.create;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.samuel.wechat.dto.ChatMemberCreateDto;
import com.samuel.wechat.entities.ChatEntity;
import com.samuel.wechat.entities.ChatMemberEntity;
import com.samuel.wechat.entities.UserEntity;
import com.samuel.wechat.mappers.CreateMapper;
import com.samuel.wechat.services.ChatService;
import com.samuel.wechat.services.UserService;

@Component
public class ChatMemberCreateMapperImpl implements CreateMapper<ChatMemberEntity, ChatMemberCreateDto> {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @Override
    public ChatMemberEntity mapFrom(ChatMemberCreateDto chatMemberCreateDto) {
        ChatEntity chatEntity = chatService.getChatById(chatMemberCreateDto.getChatId());

        if (chatEntity == null) {
            throw new IllegalArgumentException("Chat not found");
        }

        ChatMemberEntity chatMemberEntity = modelMapper.map(chatMemberCreateDto, ChatMemberEntity.class);

        chatMemberEntity.setChat(chatEntity);

        UserEntity userEntity = userService.getUserById(chatMemberCreateDto.getUserId());

        chatMemberEntity.setUser(userEntity);

        return chatMemberEntity;
    }
}
