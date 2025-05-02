package com.erp.mes.service;

import com.erp.mes.dto.BOMDTO;
import com.erp.mes.mapper.BOMMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BOMService {

    private final BOMMapper bomMapper;

    // BOM 목록 조회
    public List<BOMDTO> selectBOMList() {
        return bomMapper.selectBOMList();
    }

    // BOM 삽입
    public int insertBOM(BOMDTO bomDTO) {
        return bomMapper.insertBOM(bomDTO);
    }

    // BOM 삭제
    public int deleteBOM(int bomId) {
        return bomMapper.deleteBOM(bomId);
    }

    // 특정 Item에 대한 BOM 조회
    public List<BOMDTO> selectBOMByItemId(int itemId) {
        return bomMapper.selectBOMByItemId(itemId);
    }
}
