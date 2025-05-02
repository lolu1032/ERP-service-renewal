package com.erp.mes.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class SupplierDTO {

    private int sup_id;          // 공급업체 ID
    private int inven_id;        // 재고 ID
    private String sup_code;    // 공급업체 Code
    private String name;        // 공급업체 이름
    private String contact;     // 연락처
    private String address;     // 주소
    private String manName;     // 담당자 이름
    private String email;       // 이메일
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;          // 날짜
    private String status;      // 상태
}