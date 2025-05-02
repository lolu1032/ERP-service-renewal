package com.erp.mes.controller;

import com.erp.mes.dto.PlanDTO;
import com.erp.mes.service.PlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/item")
@RequiredArgsConstructor
@Slf4j
public class PlanController {
    private final PlanService service;
    @GetMapping("/addPlan")
    public String addPlan(Model model) {
        List<PlanDTO> list = service.selectPlan();
        model.addAttribute("list",list);
        return "item/plan";
    }
    @PostMapping("/insertForm")
    public String insertPlan(
            @RequestParam("date") String date,
            @RequestParam("itemId") String itemId,
            @RequestParam("deadline") String deadline,
            @RequestParam("quantity") String quantity,
            @RequestParam("status") String status
    ){
        Map<String,Object> map = new HashMap<>();
        map.put("date",date);
        map.put("itemId",itemId);
        map.put("leadtime",deadline);
        map.put("qty",quantity);
        map.put("status",status);
        int n = service.insertPlan(map);
        log.info("n={}",n);
        return "redirect:/item/addPlan";
    }
}
