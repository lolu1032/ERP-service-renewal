package com.erp.mes.input.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TransactionDTO {
    // 거래내역
    private int tranId; // 거래(pk)
    private String transDate; // 거래날짜
    private int val; // 거래금액
    private String status; // 거래현황
    private Instant date; // 거래일자
    private String supCode; // 협력코드

    // 입고
    private Instant expDate; // 출고예정일

    // 조달계획
    private int qty; // 계획 수량
    private Instant leadTime; // 납기일자

    // 물품
    private String itemName; // 물품이름
    private String unit; // 물품 단위
    private int price; // 물품 단가
}