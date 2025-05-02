package com.erp.mes.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;


@Getter
@Setter
@Data
public class StockDTO {

    private Boolean inputType;
    private int item_id; // 품목 ID
    private String item_code; // 품목 코드
    private String item_name;
    private String spec; // 목 규격
    private int price; // 품목 가격
    private String type; // 품목 유형
    // 자재
    private int stk_id;        // 재고번호
    private int qty;           // 재고수량
    private String loc;        // 재고위치
    private double value;      // 재고금액
    private String status;     // 재고상태
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date in_date;      // 입고일자
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date exp_date;     // 유효기간
    private int cons_qty;      // 소모수량
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date cons_date;    // 소모일자
    private String cons_loc;   // 소모위치
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private int initial_qty;
    private int incoming_quantity;
    private int outgoing_quantity;
    private int current_qty;
    private Date last_completed_date;

    public int calculateCurrentQty() {
        return this.initial_qty + this.incoming_quantity - this.outgoing_quantity;
    }
}
