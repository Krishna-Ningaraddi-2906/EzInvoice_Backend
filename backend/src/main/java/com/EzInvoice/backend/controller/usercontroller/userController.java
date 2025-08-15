package com.EzInvoice.backend.controller.usercontroller;

import com.EzInvoice.backend.dto.userDto.SignUpDto;
import com.EzInvoice.backend.service.userService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("home")
public class userController
{

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestPart("user") SignUpDto signUpDto ,
                                    @RequestPart(value="logo",required=false) MultipartFile logo) throws IOException
    {
        userService.createUser(signUpDto, logo);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
