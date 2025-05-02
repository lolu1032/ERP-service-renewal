package com.erp.mes.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Slf4j
public class InspectionDTO {
    private int ins_id; // 검수 아이디
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date; // 검수 생성 일자
    private String notice; // 비고
    private int status; // 검수 상태 값
}
