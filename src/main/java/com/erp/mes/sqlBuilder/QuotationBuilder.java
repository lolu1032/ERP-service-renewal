package com.erp.mes.sqlBuilder;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class QuotationBuilder {

    // 품목 삽입
    public String quoCreate() {
        return new SQL() {{
            INSERT_INTO("quotation");
            VALUES("item_id", "#{item_id}");
            VALUES("date", "NOW()");
            VALUES("price", "#{price}");
            VALUES("total_amount", "#{total_amount}");
        }}.toString();
    }

    public String quoList(){
        return new SQL() {{
            SELECT("*");
            FROM("quotation");
//            WHERE("status = '거래진행중'");

        }}.toString();
    }

}