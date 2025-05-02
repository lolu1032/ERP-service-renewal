package com.erp.mes.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StockReportDTO {
    private int stk_id;
    private int item_id;
    private String itemName;
    private String itemCode;
    private int totalQty;
    private String location;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expirationDate;
    private int remainingQty;
    private BigDecimal unitPrice;
    private BigDecimal totalValue;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
}