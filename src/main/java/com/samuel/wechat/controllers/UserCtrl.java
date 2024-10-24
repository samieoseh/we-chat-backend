package com.samuel.wechat.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.samuel.wechat.dto.UserDto;
import com.samuel.wechat.entities.UserEntity;
import com.samuel.wechat.mappers.Mapper;
import com.samuel.wechat.services.UserService;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserCtrl {

    private UserService userService;
    private Mapper<UserEntity, UserDto> userMapper;

    public UserCtrl(UserService userService, Mapper<UserEntity, UserDto> userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("")
    public ResponseEntity<?> getUsers() {
       try {
           List<UserEntity> userEntities = userService.getUsers();
           List<UserDto> userDtos = userEntities.stream()
                   .map(userMapper::mapTo)
                   .toList();
           return new ResponseEntity<>(userDtos, HttpStatus.OK);

       } catch (Exception e) {
              return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }    
    
}
