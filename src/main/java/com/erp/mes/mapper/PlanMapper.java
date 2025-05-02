package com.erp.mes.mapper;

import com.erp.mes.dto.PlanDTO;
import com.erp.mes.sqlBuilder.PlanBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;


@Mapper
public interface PlanMapper {
    @SelectProvider(type = PlanBuilder.class,method = "buildSelectPlan")
    List<PlanDTO> selectPlan();

    @InsertProvider(type = PlanBuilder.class,method = "buildInsertPlan")
    int insertPlan(Map<String,Object> map);
}
