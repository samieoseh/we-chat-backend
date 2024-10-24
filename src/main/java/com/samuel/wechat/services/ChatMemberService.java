package com.samuel.wechat.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samuel.wechat.entities.ChatEntity;
import com.samuel.wechat.entities.ChatMemberEntity;
import com.samuel.wechat.repositories.ChatMemberRepo;

@Service
public class ChatMemberService {

    @Autowired
    private ChatMemberRepo chatMemberRepo;

    @Autowired
    private ChatService chatService;

    public ChatMemberEntity createMember(ChatMemberEntity chatMemberEntity) {
        ChatEntity chatEntity = chatService.getChatById(chatMemberEntity.getChat().getId());

        // if chatEntity is not a group chat, then only two users can be added
        if (!chatService.isGroupChat(chatEntity.getId()) && chatMemberRepo.countByChat(chatEntity) >= 2) {
            throw new IllegalArgumentException("Chat is full");
        }
        chatMemberEntity.setJoinedAt(new Date());
        return chatMemberRepo.save(chatMemberEntity);
    }

    public List<ChatMemberEntity> getMembers(String chatId) {
        ChatEntity chatEntity = chatService.getChatById(chatId);

        if (chatEntity == null) {
            throw new IllegalArgumentException("Chat not found");
        }

        return chatMemberRepo.findByChat(chatEntity);
    }

    public void deleteMember(String chatId, String memberId) {
        ChatEntity chatEntity = chatService.getChatById(chatId);

        if (chatEntity == null) {
            throw new IllegalArgumentException("Chat not found");
        }

        ChatMemberEntity chatMemberEntity = chatMemberRepo.findById(memberId).orElse(null);

        if (chatMemberEntity == null) {
            throw new IllegalArgumentException("Member not found");
        }

        chatMemberRepo.delete(chatMemberEntity);
    }

    public ChatMemberEntity getMember(String chatId, String memberId) {
        ChatEntity chatEntity = chatService.getChatById(chatId);

        if (chatEntity == null) {
            throw new IllegalArgumentException("Chat not found");
        }

        ChatMemberEntity chatMemberEntity = chatMemberRepo.findById(memberId).orElse(null);

        if (chatMemberEntity == null) {
            throw new IllegalArgumentException("Member not found");
        }

        return chatMemberEntity;
    }

}
