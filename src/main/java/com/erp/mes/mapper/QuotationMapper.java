package com.erp.mes.mapper;

import com.erp.mes.dto.QuotationDTO;
import com.erp.mes.sqlBuilder.QuotationBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

@Mapper
public interface QuotationMapper {


    // 견적서 등록
    @InsertProvider(type = QuotationBuilder.class, method = "quoCreate")
    int quoCreate(Map<String, Object> map);

    // 견적서 조회
    @SelectProvider(type = QuotationBuilder.class, method = "quoList")
    List<QuotationDTO> quoList();
}
