package com.erp.mes.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class BOMDTO {
    private int bomId;         // BOM ID
    private int itemId;        // Item ID (외래 키)
    private String itemName;   // 품목명
    private String spec;       // 규격
    private int amount;        // 수량
    private Date createDate;   // BOM 생성일
}
