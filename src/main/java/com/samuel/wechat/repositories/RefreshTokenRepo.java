package com.samuel.wechat.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.samuel.wechat.entities.RefreshTokenEntity;

import java.util.List;

public interface RefreshTokenRepo extends CrudRepository<RefreshTokenEntity, String> {

    boolean existsByTokenAndIsActive(String refreshToken, boolean isActive);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshTokenEntity r WHERE r.userId IN :ids")
    void deleteByUserIdIn(List<String> ids);
}
