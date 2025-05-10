package com.erp.mes.input.Vo;

import java.time.Instant;

public class OrderVo {
    private int orderId;
    private String orderCode;
    private Instant date;
    private String status;
    private String value;
    private String insepStatus;
    // 외래키
    private int supId;
    private int planId;
}
