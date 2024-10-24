package com.samuel.wechat.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

import com.samuel.wechat.dto.ChatMemberCreateDto;
import com.samuel.wechat.dto.ChatMemberReadDto;
import com.samuel.wechat.dto.create.ChatCreateDto;
import com.samuel.wechat.dto.read.ChatReadDto;
import com.samuel.wechat.entities.ChatEntity;
import com.samuel.wechat.entities.ChatMemberEntity;
import com.samuel.wechat.mappers.CreateMapper;
import com.samuel.wechat.mappers.ReadMapper;
import com.samuel.wechat.services.ChatMemberService;
import com.samuel.wechat.services.ChatService;

@RestController
@RequestMapping("/api/v1/chats")
public class ChatCtrl {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ReadMapper<ChatEntity, ChatReadDto> chatReadMapper;

    @Autowired
    private CreateMapper<ChatEntity, ChatCreateDto> chatCreateMapper;

    @Autowired
    private ChatMemberService chatMemberService;

    @Autowired
    private CreateMapper<ChatMemberEntity, ChatMemberCreateDto> chatMemberCreateMapper;

    @Autowired
    private ReadMapper<ChatMemberEntity, ChatMemberReadDto> chatMemberReadMapper;

    @GetMapping("")
    public ResponseEntity<?> getChats() {
        try {
            List<ChatEntity> chatEntities = chatService.getChats();
            List<ChatReadDto> chatDtos = chatEntities.stream().map(chatReadMapper::mapTo).toList();
            return new ResponseEntity<>(chatDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("")
    public ResponseEntity<?> addChat(@RequestBody ChatCreateDto chatDto) {
        try {
            ChatEntity chatEntity = chatCreateMapper.mapFrom(chatDto);
            ChatEntity savedChatEntity = chatService.addChat(chatEntity);
            return new ResponseEntity<>(chatReadMapper.mapTo(savedChatEntity), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    
    @GetMapping("/{chatId}")
    public ResponseEntity<?> getChatById(@PathVariable String chatId) {
        try {
            System.out.println("chatId: " + chatId);
            ChatEntity chatEntity = chatService.getChatById(chatId);
            return new ResponseEntity<>(chatReadMapper.mapTo(chatEntity), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getChatsByUserId(@PathVariable String userId) {
        try {
            System.out.println("userId: " + userId);
            List<ChatEntity> chatEntities = chatService.getChatsByUserId(userId);
            List<ChatReadDto> chatDtos = chatEntities.stream().map(chatReadMapper::mapTo).toList();
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
            ChatEntity chatEntity = chatService.getChatById(chatId);
            chatEntity.setChatName(newChatName);
            ChatEntity updatedChatEntity = chatService.addChat(chatEntity);
            return new ResponseEntity<>(chatReadMapper.mapTo(updatedChatEntity), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // chat members

    @PostMapping("/{chatId}/members")
    public ResponseEntity<?> createMember(@RequestBody ChatMemberCreateDto chatMemberCreateDto) {
        try {
            ChatMemberEntity chatMemberEntity = chatMemberCreateMapper.mapFrom(chatMemberCreateDto);
            ChatMemberEntity createdChatMemberEntity = chatMemberService.createMember(chatMemberEntity);
            ChatMemberReadDto chatMemberReadDto = chatMemberReadMapper.mapTo(createdChatMemberEntity);
            return new ResponseEntity<>(chatMemberReadDto, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("User member already exists", HttpStatus.CONFLICT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{chatId}/members")
    public ResponseEntity<?> getMembers(@PathVariable String chatId) {
        try {
            List<ChatMemberEntity> chatMemberEntities = chatMemberService.getMembers(chatId);
            List<ChatMemberReadDto> chatMemberReadDtos = chatMemberEntities.stream().map(chatMemberReadMapper::mapTo)
                    .toList();
            return new ResponseEntity<>(chatMemberReadDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{chatId}/members/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable String chatId, @PathVariable String memberId) {
        try {
            chatMemberService.deleteMember(chatId, memberId);
            return new ResponseEntity<>("Member deleted successfully", HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{chatId}/members/{memberId}")
    public ResponseEntity<?> getMember(@PathVariable String chatId, @PathVariable String memberId) {
        try {
            ChatMemberEntity chatMemberEntity = chatMemberService.getMember(chatId, memberId);
            ChatMemberReadDto chatMemberReadDto = chatMemberReadMapper.mapTo(chatMemberEntity);
            return new ResponseEntity<>(chatMemberReadDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
