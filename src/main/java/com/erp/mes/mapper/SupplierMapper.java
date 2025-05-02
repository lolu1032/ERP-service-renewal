package com.erp.mes.mapper;

import com.erp.mes.dto.SupplierDTO;
import com.erp.mes.sqlBuilder.PurchaseBuilder;
import com.erp.mes.sqlBuilder.SupplierBuilder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface SupplierMapper {

    @SelectProvider(type = SupplierBuilder.class, method = "getAllSupplier")
    List<SupplierDTO> getAllSupplier();

}
