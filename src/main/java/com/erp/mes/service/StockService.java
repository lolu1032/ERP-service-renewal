package com.erp.mes.service;


import com.erp.mes.dto.ItemDTO;
import com.erp.mes.dto.StockDTO;
import com.erp.mes.mapper.StockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service

@RequiredArgsConstructor
public class StockService {
    private final StockMapper stockMapper;

    // 입고 완료된 자재를 재고로 반영
    public int insertStockFromCompletedInput() {
        return stockMapper.insertStockFromCompletedInput();
    }

    // 재고 목록 조회 (필터링 가능)
    public List<StockDTO> getStockList(Map<String, Object> params) {
        return stockMapper.selectStockList(params);
    }

    // 출고 후 재고 수량 업데이트
    public int updateStockAfterShipment(int stkId, int qty) {
        return stockMapper.updateStockAfterShipment(stkId, qty);
    }

    // 재고 상태 업데이트
    public int updateStockStatus(int stkId, String status) {
        return stockMapper.updateStockStatus(stkId, status);
    }

    // 재고 금액 산출

    public Map<String, Object> calculateStockValue(String startDate, String endDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        List<Map<String, Object>> stockDetails = stockMapper.calculateStockValue(params);

        double totalStockValue = stockDetails.stream()
                .mapToDouble(item -> Double.parseDouble(item.get("item_total_value").toString()))
                .sum();

        Map<String, Object> result = new HashMap<>();
        result.put("totalStockValue", totalStockValue);
        result.put("stockDetails", stockDetails);

        return result;
    }

    public StockDTO getStockDetails(int stkId) {
        return stockMapper.selectStockDetails(stkId);
    }

    // 공급가 정보 가져오기
    public List<Map<String, Object>> getPrice(int stkId) {
        return stockMapper.getPrice(stkId);
    }

    public List<StockDTO> getFilteredStockList(Map<String, Object> params) {
        return stockMapper.selectStockList(params); // StockMapper에 정의된 메서드 호출
    }

    public List<StockDTO> getStockItemList() {
        List<StockDTO> result = stockMapper.selectStockItemList();
        System.out.println("Stock item list size from database: " + (result != null ? result.size() : "null"));
        return result;
    }

    public List<StockDTO> stockItemList() {
        return stockMapper.stockItemList();
    }

    public List<StockDTO> calculateStock(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return stockMapper.getStockCalculation(params);
    }
}