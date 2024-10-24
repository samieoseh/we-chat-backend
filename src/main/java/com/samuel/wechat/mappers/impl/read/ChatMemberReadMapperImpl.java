package com.samuel.wechat.mappers.impl.read;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.samuel.wechat.dto.ChatMemberReadDto;
import com.samuel.wechat.entities.ChatMemberEntity;
import com.samuel.wechat.mappers.ReadMapper;

@Component
public class ChatMemberReadMapperImpl implements ReadMapper<ChatMemberEntity, ChatMemberReadDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ChatMemberReadDto mapTo(ChatMemberEntity chatMemberEntity) {
        return modelMapper.map(chatMemberEntity, ChatMemberReadDto.class);
    }

}
