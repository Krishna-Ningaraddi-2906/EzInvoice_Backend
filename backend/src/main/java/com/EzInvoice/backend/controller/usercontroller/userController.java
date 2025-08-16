package com.EzInvoice.backend.controller.usercontroller;

import com.EzInvoice.backend.dto.userDto.LoginRequestDto;
import com.EzInvoice.backend.dto.userDto.LoginResponseDto;
import com.EzInvoice.backend.dto.userDto.SignUpDto;
import com.EzInvoice.backend.security.JwtUtil;
import com.EzInvoice.backend.service.userService.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("home")
public class userController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil; // <-- add this line

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestPart("user") SignUpDto signUpDto ,
                                    @RequestPart(value="logo",required=false) MultipartFile logo) throws IOException
    {
        userService.createUser(signUpDto, logo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "SignUp Successful"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto user) {
        LoginResponseDto response = userService.login(user);

        if ("Success".equals(response.getMessage())) {
            String jwt = jwtUtil.generateToken(response.getEmail()); // use email as subject
            response.setToken(jwt);  // set JWT in response
            return ResponseEntity.ok(response); // ✅ return full DTO on success
        }


        switch (response.getMessage()) {
            case "Email and Password cannot be empty.":
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", response.getMessage()));
            case "User not found.":
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", response.getMessage()));
            case "Invalid password.":
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", response.getMessage()));
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Something went wrong"));
        }
    }

}
