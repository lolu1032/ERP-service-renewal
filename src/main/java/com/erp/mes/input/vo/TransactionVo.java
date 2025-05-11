package com.erp.mes.input.vo;

import java.time.Instant;

public class TransactionVo {
    private int tranId;
    private Instant date;
    private int val;
    private String status;
    private String supCode;
    // 외래키
    private int planId;
}
