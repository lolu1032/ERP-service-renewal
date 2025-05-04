package com.erp.mes.input.repository;

import com.erp.mes.input.dto.InputCommonDtos.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InputMapper {
    int updateInput(ItemInspecRequest itemInspecRequest);

    int updateInputStatus(InputStatusRequest request);
}
