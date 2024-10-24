package com.samuel.wechat.services;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.samuel.wechat.dto.LoginRequestDto;
import com.samuel.wechat.dto.LoginResponseDto;
import com.samuel.wechat.dto.UserDto;
import com.samuel.wechat.entities.RefreshTokenEntity;
import com.samuel.wechat.entities.UserEntity;
import com.samuel.wechat.exceptions.InvalidUsernameAndPasswordException;
import com.samuel.wechat.mappers.Mapper;
import com.samuel.wechat.repositories.RefreshTokenRepo;
import com.samuel.wechat.repositories.UserRepo;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private LoginResponseDto loginResponseDto;

    @Autowired
    private Mapper<UserEntity, UserDto> userMapper;

    @Value("${jwt.refresh-token-expiration-time}")
    private Long refreshTokenExpirationTimeInMillis;

    public UserEntity registerUser(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepo.save(userEntity);
    }

    public List<UserEntity> getUsers() {
        return StreamSupport.stream(userRepo.findAll().spliterator(), false)
                .toList();
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) throws InvalidUsernameAndPasswordException {
        Optional<UserEntity> foundUserEntity = userRepo.findByUsername(loginRequestDto.getUsername());

        if (foundUserEntity.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
           
        UserEntity user = foundUserEntity.get();

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new InvalidUsernameAndPasswordException("Invalid username and password");
        }
    
        Map<String, String> tokens = jwtService.generateToken(user.getId(), user.getUsername());


        UserDto userDto = userMapper.mapTo(user);
        loginResponseDto.setAccessToken(tokens.get("accessToken"));
        loginResponseDto.setUser(userDto);


        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", tokens.get("refreshToken"))
                .httpOnly(true)
                .path("/")
                .maxAge(refreshTokenExpirationTimeInMillis / 1000)
                .sameSite("Strict")
                .build();

        response.addHeader(
                "Set-Cookie",
                responseCookie.toString()
        );

        saveRefreshToken(tokens.get("refreshToken"));
        return loginResponseDto;

    }

    private void saveRefreshToken(String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setExpirationTime(System.currentTimeMillis() + refreshTokenExpirationTimeInMillis);
        refreshTokenEntity.setIsActive(true);
        refreshTokenEntity.setUserId(jwtService.extractId(refreshToken));
        refreshTokenRepo.save(refreshTokenEntity);
    }

    public LoginResponseDto refresh(String refreshToken) {
        if (refreshTokenRepo.existsById(refreshToken) && refreshTokenRepo.existsByTokenAndIsActive(refreshToken, true)) {
            String id = jwtService.extractId(refreshToken);
            String username = jwtService.extractUsername(refreshToken);
            Map<String, String> tokens = jwtService.generateToken(id, username);
            loginResponseDto.setAccessToken(tokens.get("accessToken"));
            return loginResponseDto;
        }
        return null;
    }

    public void revoke(String id) {
        refreshTokenRepo.deleteByUserIdIn(Arrays.asList(id));
    }

    public String extractDeviceIdFromRequest(HttpServletRequest request) {
        return request.getHeader("device-id");
    }

    public UserEntity getUserById(String userId) {
        return userRepo.findById(userId).orElse(null);
    }
}
