package com.samuel.wechat.repositories;

import org.springframework.data.repository.CrudRepository;

import com.samuel.wechat.entities.UserEntity;

import java.util.Optional;

public interface UserRepo extends CrudRepository<UserEntity, String> {
    Optional<UserEntity> findByUsername(String username);
}
