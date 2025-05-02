package com.erp.mes.restController;

import com.erp.mes.dto.ItemDTO;
import com.erp.mes.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ItemRestController {

    private final ItemService itemService;

    @ModelAttribute("servletPath")
    String getRequestServletPath(HttpServletRequest request) {
        return request.getServletPath();
    }

    public ItemRestController(ItemService itemService) {
        this.itemService = itemService;
    }

    // 품목 등록
    @PostMapping("addItem")
    public Map<String, Object> addItem(Map<String, Object> map) {
        int result = itemService.addItem(map);
        return map;
    }

    // 품목 1개 데이터 가져오기
    @PostMapping("/purchase/getItem")
    public Map<String,Object> getItem(@RequestBody Map<String, Object> map) {
        ItemDTO itemDTO = itemService.selectItemByIdOrName(map);
        map.put("itemDTO", itemDTO);
        return map;
    }

    // 계약서 등록
    @PostMapping("/item/addContract")
    public Map<String,Object> addContract(@RequestBody Map<String,Object> map){
        int status = itemService.addContract(map);
        map.put("status",status);
        return map;
    }
}
