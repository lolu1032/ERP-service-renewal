package com.erp.mes.restController;

import com.erp.mes.dto.SupplierDTO;
import com.erp.mes.service.SupplierService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SupplierRestController {

    private final SupplierService supplierService;

    public SupplierRestController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping("purchase/getAllSupplier")
    public Map<String, Object> getAllSupplier(@RequestParam Map<String,Object> map){
//        @RequestBody Map<String, Object> map
//        Map<String, Object> map = new HashMap<>();
        List<SupplierDTO> supList = supplierService.getAllSupplier();
        map.put("supList", supList);
        return map;
    }
}
