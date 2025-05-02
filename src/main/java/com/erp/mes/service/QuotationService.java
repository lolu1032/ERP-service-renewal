package com.erp.mes.service;

import com.erp.mes.dto.QuotationDTO;
import com.erp.mes.mapper.QuotationMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QuotationService {

    private final QuotationMapper quotationMapper;

    public QuotationService(QuotationMapper quotationMapper) {
        this.quotationMapper = quotationMapper;
    }

    public int quoCreate(Map<String, Object> map){
        return quotationMapper.quoCreate(map);
    };

    public List<QuotationDTO> quoList() {
        return quotationMapper.quoList();
    }
}
