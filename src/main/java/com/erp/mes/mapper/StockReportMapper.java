package com.erp.mes.mapper;

import com.erp.mes.dto.StockReportDTO;
import com.erp.mes.sqlBuilder.StockReportBuilder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StockReportMapper {
    @SelectProvider(type = StockReportBuilder.class, method = "generateStockReport")
    List<StockReportDTO> generateStockReport(Map<String, Object> params);

    @SelectProvider(type = StockReportBuilder.class, method = "calculateTotalValue")
    Double calculateTotalValue(Map<String, Object> params);

    @SelectProvider(type = StockReportBuilder.class, method = "selectStockDetails")
    StockReportDTO selectStockDetails(@Param("stkId") int stkId);
}