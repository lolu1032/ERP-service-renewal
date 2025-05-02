package com.erp.mes.sqlBuilder;

import org.apache.ibatis.jdbc.SQL;

public class BOMBuilder {

    // BOM 목록 조회 (특정 품목과 연결된 BOM 조회 가능)
    public String selectBOMList() {
        return new SQL() {{
            SELECT("b.bom_id, b.amount, b.create_date, i.item_id, i.name AS item_name, i.spec");
            FROM("BOM b");
            JOIN("item i ON b.item_id = i.item_id");
            ORDER_BY("b.create_date DESC");
        }}.toString();
    }

    // BOM 삽입
    public String insertBOM() {
        return new SQL() {{
            INSERT_INTO("BOM");
            VALUES("item_id", "#{itemId}");
            VALUES("amount", "#{amount}");
            VALUES("create_date", "NOW()");
        }}.toString();
    }

    // BOM 삭제
    public String deleteBOM() {
        return new SQL() {{
            DELETE_FROM("BOM");
            WHERE("bom_id = #{bomId}");
        }}.toString();
    }

    // 특정 Item과 연관된 BOM 조회
    public String selectBOMByItemId() {
        return new SQL() {{
            SELECT("b.bom_id, b.amount, b.create_date, i.item_id, i.name AS item_name, i.spec");
            FROM("BOM b");
            JOIN("item i ON b.item_id = i.item_id");
            WHERE("i.item_id = #{itemId}");
            ORDER_BY("b.create_date DESC");
        }}.toString();
    }
}
