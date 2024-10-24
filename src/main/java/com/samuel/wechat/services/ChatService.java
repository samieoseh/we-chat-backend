package com.samuel.wechat.services;

import java.util.Date;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samuel.wechat.entities.ChatEntity;
import com.samuel.wechat.entities.UserEntity;
import com.samuel.wechat.repositories.ChatRepo;
import com.samuel.wechat.repositories.UserRepo;

@Service
public class ChatService {
    
    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private UserRepo userRepo;


    public ChatEntity addChat(ChatEntity chatEntity) {
        chatEntity.setCreatedAt(new Date());
        return chatRepo.save(chatEntity);
    }

    public ChatEntity getChatById(String chatId) {
        return chatRepo.findById(chatId).orElse(null);
    }

    public List<ChatEntity> getChatsByUserId(String userId) {
        UserEntity userEntity = userRepo.findById(userId).orElse(null);
        Iterable<ChatEntity> chatEntities = chatRepo.findByCreatedBy(userEntity);
        return StreamSupport.stream(chatEntities.spliterator(), false).toList();
    }

    public void deleteChat(String chatId) {
        chatRepo.deleteById(chatId);
    }

    public boolean isGroupChat(String chatId) {
        return chatRepo.existsByIdAndIsGroupTrue(chatId);
    }

    public List<ChatEntity> getChats() {
        return StreamSupport.stream(chatRepo.findAll().spliterator(), false).toList();
    }
}
