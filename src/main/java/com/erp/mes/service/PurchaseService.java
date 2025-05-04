package com.erp.mes.service;

import com.erp.mes.input.domain.OrderDTO;
import com.erp.mes.dto.PlanDTO;
import com.erp.mes.dto.SupplierDTO;
import com.erp.mes.mapper.PurchaseMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PurchaseService {
    private final PurchaseMapper purchaseMapper;

    public PurchaseService(PurchaseMapper purchaseMapper) {
        this.purchaseMapper = purchaseMapper;
    }

    // 조달 계획 리스트
    public List<PlanDTO> plan() {
        return purchaseMapper.plan();
    }

    // 구매 발주서 발행
    public int orderCreate(Map<String, Object> map) {
        return purchaseMapper.orderCreate(map);
    }

    // 구매 발주서 리스트
    public List<OrderDTO> order() {
        return purchaseMapper.order();
    }

    // 구매 발주서 수정
    public int orderForm(Map<String, Object> map) {
        return purchaseMapper.orderForm(map);
    }

    // 검수 확인
    public List<OrderDTO> inspection() {
        return purchaseMapper.inspection();
    }

    // 검수 생성
    public int addInspec(Map<String, Object> map) {
        return purchaseMapper.addInspec(map);
    }

    // 검수 수정
    public int inspectionUpdate(Map<String, Object> map) {
        return purchaseMapper.inspectionUpdate(map);
    }


    public List<SupplierDTO> getSupplier() {
        return purchaseMapper.getSupplier();
    }
}
