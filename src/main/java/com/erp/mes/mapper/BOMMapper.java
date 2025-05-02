package com.erp.mes.mapper;

import com.erp.mes.dto.BOMDTO;
import com.erp.mes.sqlBuilder.BOMBuilder;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BOMMapper {

    // BOM 목록 조회
    @SelectProvider(type = BOMBuilder.class, method = "selectBOMList")
    List<BOMDTO> selectBOMList();

    // BOM 삽입
    @InsertProvider(type = BOMBuilder.class, method = "insertBOM")
    int insertBOM(BOMDTO bomDTO);

    // BOM 삭제
    @DeleteProvider(type = BOMBuilder.class, method = "deleteBOM")
    int deleteBOM(@Param("bomId") int bomId);

    // 특정 Item에 대한 BOM 조회
    @SelectProvider(type = BOMBuilder.class, method = "selectBOMByItemId")
    List<BOMDTO> selectBOMByItemId(@Param("itemId") int itemId);
}
