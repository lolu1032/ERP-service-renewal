package com.erp.mes.input.repository;

import com.erp.mes.input.domain.OrderDTO;
import com.erp.mes.input.dto.InputCommonDtos.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InputMapper {
    int updateInput(ItemInspecRequest itemInspecRequest);

    int updateInputStatus(InputStatusRequest request);

    List<OrderDTO> searchInput(String keyword);
}
