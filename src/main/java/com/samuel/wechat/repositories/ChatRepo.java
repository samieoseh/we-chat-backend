package com.samuel.wechat.repositories;

import org.springframework.data.repository.CrudRepository;

import com.samuel.wechat.entities.ChatEntity;
import com.samuel.wechat.entities.UserEntity;

import jakarta.transaction.Transactional;

public interface ChatRepo extends CrudRepository<ChatEntity, String> {

    Iterable<ChatEntity> findByCreatedBy(UserEntity userEntity);

    boolean existsByIdAndIsGroupTrue(String chatId);
    
}
