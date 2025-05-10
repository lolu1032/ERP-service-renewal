package com.erp.mes.input.Vo;

import java.time.Instant;

public class TransactionVo {
    private int tranId;
    private Instant date;
    private String val;
    private String status;
    private String supCode;
    // 외래키
    private int planId;
}
