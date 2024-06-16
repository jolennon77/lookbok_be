package org.example.lookbok_be.service;

import org.example.lookbok_be.dto.JoinDTO;
import org.example.lookbok_be.entity.UserEntity;
import org.example.lookbok_be.jwt.JWTUtil;
import org.example.lookbok_be.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JWTUtil jwtUtil;
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JWTUtil jwtUtil) {

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void registerUser(JoinDTO joinDTO) {

        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();

        Boolean isExist = userRepository.existsByUsername(username);

        if (isExist) {

            return;
        }

        UserEntity user  = new UserEntity();

        user .setUsername(username);
        user .setPassword(bCryptPasswordEncoder.encode(password));
        user .setRole("ROLE_ADMIN");

        userRepository.save(user);
    }

    public String loginUser(JoinDTO joinDTO) {
        UserEntity user = userRepository.findByUsername(joinDTO.getUsername());
        if (user != null && bCryptPasswordEncoder.matches(joinDTO.getPassword(), user.getPassword())) {
            return jwtUtil.createJwt(user.getUsername(), user.getRole(), 60 * 60 * 1000L);  // 토큰 유효 기간 설정 (예: 1시간)
        }
        throw new RuntimeException("Invalid username or password");
    }


    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}