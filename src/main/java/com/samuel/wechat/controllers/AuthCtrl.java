package com.samuel.wechat.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samuel.wechat.dto.LoginRequestDto;
import com.samuel.wechat.dto.LoginResponseDto;
import com.samuel.wechat.dto.UserDto;
import com.samuel.wechat.entities.UserEntity;
import com.samuel.wechat.mappers.Mapper;
import com.samuel.wechat.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthCtrl {

    private UserService userService;
    private Mapper<UserEntity, UserDto> userMapper;

    public AuthCtrl(UserService userService, Mapper<UserEntity, UserDto> userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody UserEntity userEntity) {
        try {
            UserEntity registerUserEntity = userService.registerUser(userEntity);
            UserDto registeredUserDto = userMapper.mapTo(registerUserEntity);
            return new ResponseEntity<>(registeredUserDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            LoginResponseDto loginResponseDto = userService.login(loginRequestDto, response);
            return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("refresh")
    public ResponseEntity<?> refresh(
            @CookieValue(name = "refreshToken", defaultValue = "default_value") String refreshToken) {
        try {
            if (refreshToken.equals("default_value")) {
                return new ResponseEntity<>("Refresh token is missing", HttpStatus.FORBIDDEN);
            }
            LoginResponseDto loginResponseDto = userService.refresh(refreshToken);
            return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/revoke/{id}")
    public ResponseEntity<?> revoke(@PathVariable String id) {
        try {
            System.out.println("id: " + id);
            userService.revoke(id);
            return new ResponseEntity<>("User refresh tokens has been revoked", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
