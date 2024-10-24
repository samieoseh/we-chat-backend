package com.samuel.wechat.mappers.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.samuel.wechat.dto.ChatDto;
import com.samuel.wechat.entities.ChatEntity;
import com.samuel.wechat.entities.UserEntity;
import com.samuel.wechat.mappers.Mapper;
import com.samuel.wechat.repositories.UserRepo;

@Component
public class ChatMapperImpl implements Mapper<ChatEntity, ChatDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Override
    public ChatDto mapTo(ChatEntity chatEntity) {
        ChatDto chatDto = modelMapper.map(chatEntity, ChatDto.class);
        System.out.println("chatDto: " + chatDto);
        chatDto.setCreatedBy(chatEntity.getCreatedBy().getId());
        return chatDto;
    }

    @Override
    public ChatEntity mapFrom(ChatDto chatDto) {
        ChatEntity chatEntity = modelMapper.map(chatDto, ChatEntity.class);
        System.out.println("chatEntity: " + chatEntity);
        
        Optional<UserEntity> userEntity = userRepo.findById(chatDto.getCreatedBy());

        chatEntity.setCreatedBy(userEntity.get());

        return chatEntity;
    }
    
}
