package com.samuel.wechat.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samuel.wechat.dto.ChatDto;
import com.samuel.wechat.entities.ChatEntity;
import com.samuel.wechat.mappers.Mapper;
import com.samuel.wechat.services.ChatService;

@RestController
@RequestMapping("/api/v1/chats")
public class ChatCtrl {

    @Autowired
    private ChatService chatService;

    @Autowired
    private Mapper<ChatEntity, ChatDto> chatMapper;

    @PostMapping("")
    public ResponseEntity<?> addChat(@RequestBody ChatDto chatDto) {
        try {
            ChatEntity chatEntity = chatMapper.mapFrom(chatDto);
            ChatEntity savedChatEntity = chatService.addChat(chatEntity);
            return new ResponseEntity<>(chatMapper.mapTo(savedChatEntity), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    
    @GetMapping("/{chatId}")
    public ResponseEntity<?> getChatById(@PathVariable String chatId) {
        try {
            System.out.println("chatId: " + chatId);
            ChatEntity chatEntity = chatService.getChatById(chatId);
            return new ResponseEntity<>(chatMapper.mapTo(chatEntity), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getChatsByUserId(@PathVariable String userId) {
        try {
            System.out.println("userId: " + userId);
            List<ChatEntity> chatEntities = chatService.getChatsByUserId(userId);
            List<ChatDto> chatDtos = chatEntities.stream().map(chatMapper::mapTo).toList();
            return new ResponseEntity<>(chatDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<?> deleteChat(@PathVariable String chatId) {
        try {
            chatService.deleteChat(chatId);
            return new ResponseEntity<>("Chat deleted successfully", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PatchMapping("/{chatId}/rename")
    public ResponseEntity<?> updateChat(@PathVariable String chatId, @RequestBody String newChatName) {
        try {
            System.out.println("chatId: " + chatId);
            ChatEntity chatEntity = chatService.getChatById(chatId);
            System.out.println("newChatName: " + newChatName);
            chatEntity.setChatName(newChatName);
            ChatEntity updatedChatEntity = chatService.addChat(chatEntity);
            System.out.println("updatedChatEntity: " + updatedChatEntity);
            return new ResponseEntity<>(chatMapper.mapTo(updatedChatEntity), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
