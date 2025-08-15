package com.EzInvoice.backend.service.userService;

import com.EzInvoice.backend.dto.userDto.SignUpDto;
import com.EzInvoice.backend.model.UserEntity;
import com.EzInvoice.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserService
{
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    public void createUser(SignUpDto user, MultipartFile logo) throws IOException {
        UserEntity userEntity = new UserEntity();

        userEntity.setUserName(user.getUserName());
        userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setCompanyName(user.getCompanyName());

        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        userEntity.setEmail(user.getEmail());
        userEntity.setContactNo(user.getContactNo());

        if (logo != null && !logo.isEmpty()) {
            userEntity.setLogo(logo.getBytes()); // byte[] field in UserEntity
        }

        userRepository.save(userEntity);

    }
}
