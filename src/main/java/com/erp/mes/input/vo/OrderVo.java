package com.erp.mes.input.vo;

import java.time.Instant;

public class OrderVo {
    private int orderId;
    private String orderCode;
    private Instant date;
    private String status;
    private int value;
    private String insepStatus;
    private String selectValue;
    // 외래키
    private int supId;
    private int planId;
}
