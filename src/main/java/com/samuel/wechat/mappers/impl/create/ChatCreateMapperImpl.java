package com.samuel.wechat.mappers.impl.create;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.samuel.wechat.dto.create.ChatCreateDto;
import com.samuel.wechat.entities.ChatEntity;
import com.samuel.wechat.entities.UserEntity;
import com.samuel.wechat.mappers.CreateMapper;
import com.samuel.wechat.repositories.UserRepo;

@Component
public class ChatCreateMapperImpl implements CreateMapper<ChatEntity, ChatCreateDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Override
    public ChatEntity mapFrom(ChatCreateDto chatDto) {
        ChatEntity chatEntity = modelMapper.map(chatDto, ChatEntity.class);
        System.out.println("chatEntity: " + chatEntity);

        Optional<UserEntity> userEntity = userRepo.findById(chatDto.getCreatedBy());

        chatEntity.setCreatedBy(userEntity.get());

        return chatEntity;
    }

}
