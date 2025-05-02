package com.erp.mes.controller;

import com.erp.mes.dto.QuotationDTO;
import com.erp.mes.service.QuotationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
public class QuotationController {

    private final QuotationService quotationService;

    @ModelAttribute("servletPath")
    String getRequestServletPath(HttpServletRequest request) {
        return request.getServletPath();
    }

    public QuotationController(QuotationService quotationService) {
        this.quotationService = quotationService;
    }
    // 견적서 리스트
    @GetMapping(value = "item/quoList")
    public String quoList(Map<String, Object> map){
        List<QuotationDTO> quoList = quotationService.quoList();
        map.put("quoList",quoList);
        return "item/quoList";
    }



}
