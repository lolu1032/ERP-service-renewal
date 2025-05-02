package com.erp.mes.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/member")
public class MemberController {
    /**
     * 로그인 창 이동
     * @return
     */
    @GetMapping(value = "login")
    public String loginForm() {
        return "member/login";
    }

    @GetMapping("register")
    public String register() {
        return "member/register";
    }

}
