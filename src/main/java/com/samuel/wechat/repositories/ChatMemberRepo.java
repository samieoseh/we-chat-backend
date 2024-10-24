package com.samuel.wechat.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.samuel.wechat.entities.ChatEntity;
import com.samuel.wechat.entities.ChatMemberEntity;

public interface ChatMemberRepo extends CrudRepository<ChatMemberEntity, String> {

    int countByChat(ChatEntity chatEntity);

    List<ChatMemberEntity> findByChat(ChatEntity chatEntity);

}
