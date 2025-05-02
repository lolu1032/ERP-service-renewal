package com.erp.mes.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class LinkController {
    @ModelAttribute("servletPath")
    String getRequestServletPath(HttpServletRequest request) {
        return request.getServletPath();
    }

    @GetMapping("/error/accessError")
    public String accessError() {
        return "error/accessError";
    }

//    @GetMapping("/item/itemList")
//    public String itemList() {
//        return "item/itemList";
//    }
}
