package com.erp.mes.mapper;

import com.erp.mes.dto.ItemDTO;
import com.erp.mes.sqlBuilder.ItemBuilder;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface ItemMapper {

    // 품목 목록 조회
    @SelectProvider(type = ItemBuilder.class, method = "selectItemList")
    List<ItemDTO> selectItemList(Map<String, Object> params);

    // 품목 삽입
    @InsertProvider(type = ItemBuilder.class, method = "addItem")
    int addItem(Map<String, Object> map);

    // 품목 수정
    @UpdateProvider(type = ItemBuilder.class, method = "updateItem")
    int updateItem(ItemDTO itemDTO);

    // 품목 삭제
    @DeleteProvider(type = ItemBuilder.class, method = "deleteItem")
    int deleteItem(@Param("itemId") int itemId);

    // 유효성 검사
    @SelectProvider(type = ItemBuilder.class, method = "validateItemData")
    int validateItemData(@Param("name") String name, @Param("spec") String spec);

    // 품목 상세 조회
    @SelectProvider(type = ItemBuilder.class, method = "selectItemById")
    ItemDTO selectItemById(@Param("itemId") int itemId);

    @SelectProvider(type = ItemBuilder.class, method = "selectItemByIdOrName")
    ItemDTO selectItemByIdOrName(Map<String, Object> map);

    @InsertProvider(type = ItemBuilder.class, method = "addContract")
    int addContract(Map<String, Object> map);
}
