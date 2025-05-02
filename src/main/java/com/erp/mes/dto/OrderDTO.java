package com.erp.mes.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Slf4j
public class OrderDTO {
    private int orderId;
    private int inputId;
    private String type;
    private String keyword;
    // 발주서
    private LocalDate date; // 발주일자
    private String status; // 발주상태
    private String value; // 발주금액
    private String orderCode; // 발주코드
    private String supId; // 회사 외래키
    private String planId; // 발주계획 외래키

    // 물픔
    private String itemName; // 물품이름
    private int price; // 가격
    private String unit; // 단위

    // 조달 계획
    private int qty; // 수량
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate leadtime;// 발주납기일

    // 협력 회사
    private String supName;
    private int sup_id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recDate;
}