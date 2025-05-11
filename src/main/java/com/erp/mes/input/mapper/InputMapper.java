package com.erp.mes.input.mapper;

import com.erp.mes.input.domain.InputDTO;
import com.erp.mes.input.domain.OrderDTO;
import com.erp.mes.input.domain.TransactionDTO;
import com.erp.mes.input.dto.InputCommonDtos.*;
import com.erp.mes.input.vo.InputVo;
import com.erp.mes.input.vo.OrderVo;
import com.erp.mes.input.vo.TransactionVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InputMapper {

    // order
    // vo -> dto
    OrderVo toOrderVo(InputStatusRequest request);

}
