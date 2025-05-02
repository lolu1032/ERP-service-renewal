package com.erp.mes.sqlBuilder;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class ItemBuilder {

    // 품목 목록 조회 (검색 필터 포함, 자동 완성 가능)
    public String selectItemList(Map<String, Object> params) {
        if (params == null) {
            throw new IllegalArgumentException("Params map cannot be null");
        }

        return new SQL() {{
            // SELECT 절
            SELECT("i.item_id as itemId, i.item_code, i.name, i.spec, i.unit, i.price, i.create_date as createDate, p.leadtime");
            // FROM 절
            FROM("item i");
            // plan 테이블과 조인
            JOIN("plan p ON p.item_id = i.item_id");
            // supplier 테이블과 조인 (수정된 부분: supplier는 plan 테이블과 직접 연결되지 않으므로 적절히 수정)
//            JOIN("supplier s ON s.sup_id = p.sup_id");
            // 필터: 품목 아이디
            Integer itemId = (Integer) params.get("itemId");
            if (itemId != null) {
                WHERE("i.item_id = #{itemId}");
            }
            // 필터: 품목 이름 (keyword)
            String keyword = (String) params.get("keyword");
            if (keyword != null && !keyword.isEmpty()) {
                WHERE("i.name LIKE CONCAT('%', #{keyword}, '%')");
            }
            // 필터: 공급업체 이름
//            String supplierName = (String) params.get("supplier_name");
//            if (supplierName != null && !supplierName.isEmpty()) {
//                WHERE("s.name = #{supplier_name}");
//            }
            // 필터: 날짜 범위 (plan의 leadtime 기준)
            String startDate = (String) params.get("startDate");
            String endDate = (String) params.get("endDate");
            if (startDate != null && !startDate.isEmpty()) {
                WHERE("p.leadtime >= #{startDate}");
            }
            if (endDate != null && !endDate.isEmpty()) {
                WHERE("p.leadtime <= #{endDate}");
            }
            // 결과 정렬
            ORDER_BY("i.name ASC");
        }}.toString();
    }

    // 품목 삽입
    public String addItem() {
        return new SQL() {{
            INSERT_INTO("item");
            VALUES("name", "#{name}");
            VALUES("spec", "#{spec}");
            VALUES("unit", "#{unit}");
            VALUES("price", "#{price}");
            VALUES("create_date", "NOW()"); // NOW() syntax is database-specific, adjust as per DB
        }}.toString();
    }

    // 품목 수정
    public String updateItem() {
        return new SQL() {{
            UPDATE("item");
            SET("name = #{name}");
            SET("spec = #{spec}");
            SET("unit = #{unit}");
            SET("price = #{price}");
            WHERE("item_id = #{itemId}");
        }}.toString();
    }

    // 품목 삭제
    public String deleteItem() {
        return new SQL() {{
            DELETE_FROM("item");
            WHERE("item_id = #{itemId}");
        }}.toString();
    }

    // 유효성 검사 (중복된 품목명이나 필수 입력 누락 여부)
    public String validateItemData(Map<String, Object> params) {
        if (params == null) {
            throw new IllegalArgumentException("Params map cannot be null");
        }

        return new SQL() {{
            SELECT("COUNT(*)");
            FROM("item");
            WHERE("name = #{name}");
            AND();
            WHERE("spec = #{spec}");
        }}.toString();
    }

    // 품목 상세 조회
    public String selectItemById() {
        return new SQL() {{
            SELECT("item_id, name, spec, unit, price, create_date");
            FROM("item");
            WHERE("item_id = #{itemId}");
        }}.toString();
    }

    public String selectItemByIdOrName(Map<String, Object> map) {
        if (map == null) {
            throw new IllegalArgumentException("Params map cannot be null");
        }
        return new SQL() {{
            // item_id와 item_name을 가져옴
            String item_id = (String) map.get("item_id");
            String item_name = (String) map.get("item_name");

            // 선택할 컬럼을 지정
            SELECT("item_id, name, spec, unit, price, create_date");
            FROM("item");

            // item_id가 존재하면 WHERE 조건 추가
            if (item_id != null && !item_id.isEmpty()) {
                WHERE("item_id = #{itemId}");
            } else {
                if (item_name != null && !item_name.isEmpty()) {
                    WHERE("name = #{item_name}");
                }
            }
        }}.toString();
    }

    // 계약서 등록
    public String addContract() {
        return new SQL() {{
            INSERT_INTO("contract");
            VALUES("sup_id", "#{sup_id}");
            VALUES("date", "NOW()");
            VALUES("d_day", "#{d_day}");
            VALUES("val", "#{total_amount}");
            VALUES("status", "'계약완료'"); // NOW() syntax is database-specific, adjust as per DB
        }}.toString();
    }
}