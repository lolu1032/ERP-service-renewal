package com.erp.mes.input.repository;

import com.erp.mes.input.domain.OrderDTO;
import com.erp.mes.input.dto.InputCommonDtos.*;
import com.erp.mes.input.vo.OrderVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface InputMyBatisMapper {
    int updateInput(ItemInspecRequest itemInspecRequest);

    int updateInputStatus(OrderVo request);

    List<OrderDTO> searchInput(String keyword);

    int updateTrans(List<OrderCode> list);

    List<OrderDTO> selectPaging(Map<String,Integer> map);

    List<OrderDTO> selectOrders();

    List<OrderDTO> selectTransList();
}
