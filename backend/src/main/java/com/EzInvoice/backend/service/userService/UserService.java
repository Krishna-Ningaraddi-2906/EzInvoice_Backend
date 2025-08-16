package com.EzInvoice.backend.service.userService;

import com.EzInvoice.backend.dto.userDto.LoginRequestDto;
import com.EzInvoice.backend.dto.userDto.LoginResponseDto;
import com.EzInvoice.backend.dto.userDto.SignUpDto;
import com.EzInvoice.backend.model.UserEntity;
import com.EzInvoice.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails; // NEW
import org.springframework.security.core.userdetails.UserDetailsService; // NEW
import org.springframework.security.core.userdetails.UsernameNotFoundException; // NEW
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    // CHANGED: implement UserDetailsService

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    // ---------------- Existing Signup Method ----------------

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

    // ---------------- Existing Login Method ----------------

    public LoginResponseDto login(LoginRequestDto user) {
        if (user.getEmail() == null || user.getPassword() == null) {
            return LoginResponseDto.builder()
                    .message("Email and Password cannot be empty.")
                    .build();
        }

        Optional<UserEntity> foundUser = userRepository.findByEmail(user.getEmail());

        if (foundUser.isEmpty()) {
            // old: new LoginResponseDto("User not found.", null, null, null);
            return LoginResponseDto.builder()
                    .message("User not found.")
                    .build();
        }

        if (!bCryptPasswordEncoder.matches(user.getPassword(), foundUser.get().getPassword())) {
            // old: new LoginResponseDto("Invalid password.", null, null, null);
            return LoginResponseDto.builder()
                    .message("Invalid password.")
                    .build();
        }

        // ✅ success
        return new LoginResponseDto(
                foundUser.get().getUserName(),
                foundUser.get().getEmail(),
                "Success",
                null // token will be set in controller
        );
    }


    // ---------------- NEW: Required by Spring Security ----------------
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("USER") // simple role
                .build();
    }

    // ---------------- NEW: Convenience method for JWT filter ----------------
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        return loadUserByUsername(email);
    }
}
