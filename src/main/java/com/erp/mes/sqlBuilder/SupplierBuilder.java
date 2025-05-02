package com.erp.mes.sqlBuilder;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class SupplierBuilder {

    // 협력업체 리스트
    public String getAllSupplier(){
        return new SQL() {{
            SELECT("*");
            FROM("supplier");
        }}.toString();
    }
}
