package com.erp.mes.input.Vo;

import java.time.Instant;

public class InputVo {

    private int inputId;
    private String type;
    private Instant expDate;
    private Instant recDate;
    // 외래키
    private int tranId;
}
