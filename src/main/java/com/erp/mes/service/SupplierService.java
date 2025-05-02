package com.erp.mes.service;

import com.erp.mes.dto.SupplierDTO;
import com.erp.mes.mapper.SupplierMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierMapper supplierMapper;

    public SupplierService(SupplierMapper supplierMapper) {
        this.supplierMapper = supplierMapper;
    }

    public List<SupplierDTO> getAllSupplier(){
        return supplierMapper.getAllSupplier();
    }

}
