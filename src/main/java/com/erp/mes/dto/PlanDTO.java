package com.erp.mes.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Slf4j
public class PlanDTO {
    // 조달 계획
    private int plan_id; // 조달 계획 아이디
    private int qty; // 수량
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date; // 계획일자
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date leadtime; // 납기일자
    private String status; // 상태
    // 품목
    private int item_id; // 품목 아이디
    private String item_name; // 품목 이름
    private int price;
    // 협력회사
    private String supName;
}
