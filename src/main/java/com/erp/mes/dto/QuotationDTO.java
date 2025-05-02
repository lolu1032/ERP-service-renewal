package com.erp.mes.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Slf4j
public class QuotationDTO {
    private int quo_id;
    private int item_id;
    @DateTimeFormat(pattern ="yy-MM-dd")
    private LocalDate date;
    private int price;
    private int total_amount;
    private String status;

}
