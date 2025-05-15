package com.erp.mes.input.mapper;

import com.erp.mes.input.dto.InputCommonDtos.*;

import com.erp.mes.input.vo.OrderVo;

import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface InputMapper {

    // order
    // vo -> dto
    OrderVo toOrderVo(InputStatusRequest request);

}
