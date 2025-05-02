package com.erp.mes.service;

import com.erp.mes.dto.ItemDTO;
import com.erp.mes.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemMapper itemMapper;


    // 품목 목록 조회
    public List<ItemDTO> selectItemList(Map<String,Object> map) {
        return itemMapper.selectItemList(map);
    }

    // 품목 삽입
    public int addItem(Map<String, Object> map) {
        return itemMapper.addItem(map);
    }

    // 품목 수정
    public int updateItem(ItemDTO itemDTO) {
        return itemMapper.updateItem(itemDTO);
    }

    // 품목 삭제
    public int deleteItem(int itemId) {
        return itemMapper.deleteItem(itemId);
    }

    // 품목 상세 조회
    public ItemDTO selectItemById(int itemId) {
        return itemMapper.selectItemById(itemId);
    }
    // 품목 상세 아이디 및 이름 초이스 검색
    public ItemDTO selectItemByIdOrName(Map<String, Object> map) {
        return itemMapper.selectItemByIdOrName(map);
    }

    // 계약서 등록
    public int addContract(Map<String, Object> map) {
        System.out.println(map.get("sup_id"));
        System.out.println(map.get("d_day"));
        System.out.println(map.get("total_amount"));
        return itemMapper.addContract(map);
    }
}
