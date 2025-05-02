package com.erp.mes.service;

import com.erp.mes.dto.PlanDTO;
import com.erp.mes.mapper.PlanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanMapper mapper;

    public List<PlanDTO> selectPlan(){
        return mapper.selectPlan();
    }
    public int insertPlan(Map<String,Object> map) {
        return mapper.insertPlan(map);
    }
}
