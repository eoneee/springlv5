package com.example.springlv5.controller;

import com.example.springlv5.entity.UserRoleEnum;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class TestController {

    @Secured(value = UserRoleEnum.Authority.ADMIN)
    //Admin권한만 테스트 가능
    @PostMapping("/test-secured")
    public String securedTest(@AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("*********************************************************");
        System.out.println("UserController.securedTest");
        System.out.println("userDetails.getUsername() = " + userDetails.getUsername());
        System.out.println("*********************************************************");

        return "redirect:/api/user/login-page";
    }
}