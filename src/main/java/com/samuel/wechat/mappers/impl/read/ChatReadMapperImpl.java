package com.samuel.wechat.mappers.impl.read;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.samuel.wechat.dto.read.ChatReadDto;
import com.samuel.wechat.entities.ChatEntity;
import com.samuel.wechat.mappers.ReadMapper;

@Component
public class ChatReadMapperImpl implements ReadMapper<ChatEntity, ChatReadDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ChatReadDto mapTo(ChatEntity chatEntity) {
        return modelMapper.map(chatEntity, ChatReadDto.class);
    }
}
