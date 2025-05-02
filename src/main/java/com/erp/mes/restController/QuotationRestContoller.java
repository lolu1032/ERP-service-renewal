package com.erp.mes.restController;

import com.erp.mes.service.QuotationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/quotation")
public class QuotationRestContoller {

    private final QuotationService quotationService;

    public QuotationRestContoller(QuotationService quotationService) {
        this.quotationService = quotationService;
    }

    @PostMapping(value = "/quoCreate")
    public Map<String,Object> quoCreate(@RequestBody Map<String, Object> map){
        int status = quotationService.quoCreate(map);
        map.put("status",status);
        return map;
    };
}
