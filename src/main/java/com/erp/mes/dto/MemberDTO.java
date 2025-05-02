package com.erp.mes.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberDTO {
    private int m_code;
    private String id;
    private String pwd;
    private String name;
    private String phone_number;
    private String email;
    private String dept_name;
    private int auth;
    private int status;
}
