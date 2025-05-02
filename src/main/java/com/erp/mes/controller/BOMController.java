package com.erp.mes.controller;

import com.erp.mes.dto.BOMDTO;
import com.erp.mes.service.BOMService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bom")
public class BOMController {

    private final BOMService bomService;

    @ModelAttribute("servletPath")
    String getRequestServletPath(HttpServletRequest request) {
        return request.getServletPath();
    }

    // BOM 목록 조회
    @GetMapping("/list")
    public String selectBOMList(Model model) {
        List<BOMDTO> bomList = bomService.selectBOMList();
        model.addAttribute("bomList", bomList);
        return "bom/bomList";  // bomList.html로 반환
    }

    // BOM 등록 폼
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("bomDTO", new BOMDTO());
        return "bom/bomForm";
    }

    // BOM 등록
    @PostMapping("/add")
    public String insertBOM(@ModelAttribute BOMDTO bomDTO) {
        bomService.insertBOM(bomDTO);
        return "redirect:/bom/list";
    }

    // BOM 삭제
    @GetMapping("/delete/{bomId}")
    public String deleteBOM(@PathVariable("bomId") int bomId) {
        bomService.deleteBOM(bomId);
        return "redirect:/bom/list";
    }

    // 특정 Item과 연관된 BOM 조회
    @GetMapping("/item/{itemId}")
    public String selectBOMByItemId(@PathVariable("itemId") int itemId, Model model) {
        List<BOMDTO> bomList = bomService.selectBOMByItemId(itemId);
        model.addAttribute("bomList", bomList);
        return "bom/bomList";
    }
}
