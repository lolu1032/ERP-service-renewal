package com.erp.mes.sqlBuilder;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class PurchaseBuilder {
    // 조달계획 리스트
    public String selectPlans() {
        return new SQL() {{
            SELECT("plan.date, plan.qty, plan.leadtime, plan.status, item.name AS item_name, item.price, item.item_id");
            FROM("plan");
            JOIN("item ON item.item_id = plan.item_id");
        }}.toString();
    }

    // 발주서 발행
    public String orderCreate(Map<String, Object> map) {
        return new SQL() {{
            INSERT_INTO("`order`");
            VALUES("plan_id", "#{plan_id}");
            VALUES("sup_id", "#{sup_id}");
            VALUES("order_code", "#{order_code}");
            VALUES("date", "NOW()");
            VALUES("status", "#{status}");
            VALUES("value", "#{value}");
        }}.toString();
    }

    // 발주서 리스트
    public String selectOrders(Map<String, Object> params) {
        return new SQL() {{
            SELECT("o.order_id as orderId ,o.order_code as orderCode, i.name as itemName, p.qty, p.leadtime, s.name AS supName, o.value, o.status");
            FROM("`order` o");
            JOIN("supplier s ON s.sup_id = o.sup_id");
            JOIN("plan p ON p.plan_id = o.plan_id");
            JOIN("item i ON i.item_id = p.item_id");
            WHERE("o.status = '발주완료' OR o.status = '검수진행중'");
            WHERE("o.insep_status = 0");
        }}.toString();
    }

    // 발주서 수정
    public String updateOrder(Map<String, Object> map) {
        return new SQL() {{
            UPDATE("`order`");
            SET("leadtime = #{leadtime}");
            SET("status = #{status}");
            SET("value = #{value}");
            WHERE("order_id = #{order_id}");
        }}.toString();
    }

    // 검수 리스트 확인
    public String selectInspections() {
        return new SQL() {{
            SELECT("o.order_id AS orderId, o.order_code AS orderCode, i.name AS itemName, p.qty, p.leadtime, s.name AS supName, o.value, o.status");
            FROM("`order` o");
            JOIN("supplier s ON s.sup_id = o.sup_id");
            JOIN("plan p ON p.plan_id = o.plan_id");
            JOIN("item i ON i.item_id = p.item_id");
            WHERE("o.insep_status = 1");
        }}.toString();
    }

    // 검수 등록
    public String addInspec(Map<String, Object> map) {
        return new SQL() {{
            INSERT_INTO("inspection");
            VALUES("date", "NOW()");
            VALUES("status", "'검수진행중'");
            VALUES("notice", "#{notice}");
        }}.toString();
    }

    // 검수 수정
    public String inspectionUpdate(Map<String, Object> map) {
        return new SQL() {{
            UPDATE("`order`");
            SET("status = #{status}");
            SET("insep_status = 1");
            WHERE("order_id = #{orderId}");
        }}.toString();
    }

    // 협력업체 리스트
    public String getSupplier(Map<String, Object> map){
        return new SQL() {{
            SELECT("*");
            FROM("supplier");
        }}.toString();
    }
}
