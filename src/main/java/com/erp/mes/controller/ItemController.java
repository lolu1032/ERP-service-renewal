package com.erp.mes.controller;

import com.erp.mes.dto.ItemDTO;
import com.erp.mes.dto.StockDTO;
import com.erp.mes.service.ItemService;
import com.erp.mes.service.StockService;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/item/*")
@Slf4j
public class ItemController {

    private final ItemService itemService;

    private final StockService stockService;

    @ModelAttribute("servletPath")
    String getRequestServletPath(HttpServletRequest request) {
        return request.getServletPath();
    }

    // 소요 품목 목록 조회
    @GetMapping("itemList")
    public String itemList(Map<String, Object> map) {
        // 서비스 호출 및 필터링 결과 전달
        List<StockDTO> stockList = stockService.stockItemList();
        System.out.println(stockList);
        map.put("stockList", stockList);
        return "item/itemList";
    }

    // 품목 등록 폼
//    @GetMapping("add")
//    public String showAddForm(Model model) {
//        model.addAttribute("itemDTO", new ItemDTO());
//        return "item/itemForm";
//    }



    // 품목 수정 폼
    @GetMapping("/edit/{itemId}")
    public String showEditForm(@PathVariable("itemId") int itemId, Model model) {
        ItemDTO itemDTO = itemService.selectItemById(itemId);
        model.addAttribute("itemDTO", itemDTO);
        return "item/itemForm";
    }

    // 품목 수정
    @PostMapping("/edit")
    public String updateItem(@ModelAttribute ItemDTO itemDTO) {
        itemService.updateItem(itemDTO);
        return "redirect:/item/list";
    }

    // 품목 삭제
    @GetMapping("/delete/{itemId}")
    public String deleteItem(@PathVariable("itemId") int itemId) {
        itemService.deleteItem(itemId);
        return "redirect:/item/list";
    }

    // 품목 상세 조회
    @GetMapping("/view/{itemId}")
    public String viewItemDetails(@PathVariable("itemId") int itemId, Model model) {
        ItemDTO itemDTO = itemService.selectItemById(itemId);
        model.addAttribute("itemDTO", itemDTO);
        return "item/itemView"; // itemView.html로 반환
    }

    @PostMapping("/selectItemByIdOrName")
    public Map<String,Object> selectItemByIdOrName(@RequestBody Map<String,Object> map){
        System.out.println(map.get("item_name"));
        ItemDTO itemDTO = itemService.selectItemByIdOrName(map);
        map.put("itemDTO",itemDTO);
        return map;
    }



}
