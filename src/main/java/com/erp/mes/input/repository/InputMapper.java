package com.erp.mes.input.repository;

import com.erp.mes.input.domain.OrderDTO;
import com.erp.mes.input.dto.InputCommonDtos.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

@Mapper
public interface InputMapper {
    int updateInput(ItemInspecRequest itemInspecRequest);

    int updateInputStatus(InputStatusRequest request);

    List<OrderDTO> searchInput(String keyword);

    int updateTrans(List<OrderCode> list);

    List<OrderDTO> selectPaging(Map<String,Integer> map);
}
