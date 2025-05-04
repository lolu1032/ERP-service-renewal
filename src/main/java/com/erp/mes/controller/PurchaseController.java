package com.erp.mes.controller;

import com.erp.mes.input.domain.OrderDTO;
import com.erp.mes.dto.PlanDTO;
import com.erp.mes.service.PurchaseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class PurchaseController {
    @ModelAttribute("servletPath")
    String getRequestServletPath(HttpServletRequest request) {
        return request.getServletPath();
    }

    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping(value = "purchase/plan")
    public String plan(Map<String, Object> map) {
        List<PlanDTO> planList = purchaseService.plan();
        map.put("planList", planList);
        logger.info(planList + "");
        return "/purchase/plan";
    }

    @GetMapping(value = "purchase/order")
    public String order(Map<String,Object> map){
        List<OrderDTO> orderList = purchaseService.order();
        map.put("orderList",orderList);
        logger.info(orderList + "");
        return "/purchase/order";
    }

    @GetMapping(value = "purchase/inspection")
    public String inspection(Map<String, Object> map){
        List<OrderDTO> orderList = purchaseService.inspection();
        map.put("orderList",orderList);
        logger.info(orderList + "");
        return "/purchase/inspection";
    }


}
