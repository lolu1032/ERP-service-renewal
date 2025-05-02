package com.erp.mes.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ShipmentDTO {
    private int shipId;        // 출고 ID
    private int stkId;         // 재고 ID
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reqDate; // 요청 날짜
    private Integer reqQty;    // 요청 수량
    private Integer qty;       // 실제 출고 수량
    private String status;     // 출고 상태
    private String loc;        // 출고 위치
    private String itemName;   // 품목 이름 JOIN
    private Integer availableQty; // 재고 수량 JOIN

}