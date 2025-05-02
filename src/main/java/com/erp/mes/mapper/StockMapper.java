package com.erp.mes.mapper;

import com.erp.mes.dto.StockDTO;
import com.erp.mes.sqlBuilder.StockBuilder;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface StockMapper {

    // 입고 완료된 자재를 재고로 반영
    @InsertProvider(type = StockBuilder.class, method = "insertStockFromCompletedInput")
    int insertStockFromCompletedInput();

    // 재고 목록 조회 (필터링 처리) - Map<String, Object>로 통일
    @SelectProvider(type = StockBuilder.class, method = "selectStockList")
    List<StockDTO> selectStockList(Map<String, Object> params);

    // 출고 후 재고 수량 업데이트
    @UpdateProvider(type = StockBuilder.class, method = "updateStockAfterShipment")
    int updateStockAfterShipment(@Param("stkId") int stkId, @Param("qty") int qty);

    // 재고 상태 업데이트
    @UpdateProvider(type = StockBuilder.class, method = "updateStockStatus")
    int updateStockStatus(@Param("stkId") int stkId, @Param("status") String status);

    // 재고 금액 산출
    @SelectProvider(type = StockBuilder.class, method = "calculateStockValue")
    List<Map<String, Object>> calculateStockValue(Map<String, Object> params);
    // 공급가 확인
    @SelectProvider(type = StockBuilder.class, method = "getSupplyPrice")
    List<Map<String, Object>> getPrice(@Param("stkId") int stkId);

    @SelectProvider(type = StockBuilder.class, method = "selectStockDetails")
    StockDTO selectStockDetails(@Param("stkId") int stkId);

    // 발주 품목 소요 자재 확인
    @SelectProvider(type = StockBuilder.class, method = "selectStockItemList")
    List<StockDTO> selectStockItemList();

    // 재고 계산?
    @SelectProvider(type = StockBuilder.class, method = "getStockCalculation")
    List<StockDTO> getStockCalculation(Map<String, Object> params);

    // 재고 리스트 도출(발주 계획)
    @SelectProvider(type = StockBuilder.class, method = "stockItemList")
    List<StockDTO> stockItemList();
}