package com.erp.mes.sqlBuilder;

import org.apache.ibatis.jdbc.SQL;
import java.util.Map;

public class ShipmentBuilder {

    public String insertShipment() {
        return new SQL() {{
            INSERT_INTO("shipment");
            VALUES("stk_id", "#{stkId}");
            VALUES("req_date", "#{reqDate}");
            VALUES("req_qty", "#{reqQty}");
            VALUES("qty", "#{qty}");
            VALUES("status", "#{status}");
            VALUES("loc", "#{loc}");

        }}.toString();
    }

    public String selectShipmentList(Map<String, Object> params) {
        return new SQL() {{
            SELECT("sh.ship_id, sh.stk_id, sh.req_date, sh.req_qty, sh.qty, sh.status, sh.loc, i.name AS item_name, s.qty AS available_qty");
            FROM("shipment sh");
            INNER_JOIN("stock s ON sh.stk_id = s.stk_id");
            INNER_JOIN("item i ON s.item_id = i.item_id");

            if (params.containsKey("itemName") && params.get("itemName") != null && !params.get("itemName").toString().isEmpty()) {
                WHERE("i.name LIKE CONCAT('%', #{itemName}, '%')");
            }
            if (params.containsKey("status") && params.get("status") != null && !params.get("status").toString().isEmpty()) {
                WHERE("sh.status = #{status}");
            }
            if (params.containsKey("reqDate") && params.get("reqDate") != null && !params.get("reqDate").toString().isEmpty()) {
                WHERE("DATE(sh.req_date) = DATE(#{reqDate})");
            }

            ORDER_BY("sh.req_date DESC");
        }}.toString();
    }
    public String updateShipmentStatusToCompleted() {
        return new SQL() {{
            UPDATE("shipment");
            SET("status = 'COMPLETED'");
            SET("qty = #{qty}");
            WHERE("ship_id = #{shipId}");
        }}.toString();
    }

    public String cancelShipment() {
        return new SQL() {{
            UPDATE("shipment");
            SET("status = 'CANCELLED'");
            WHERE("ship_id = #{shipId}");
        }}.toString();
    }

    public String updateShipmentStatus() {
        return new SQL() {{
            UPDATE("shipment");
            SET("status = #{status}");
            WHERE("ship_id = #{shipId}");
        }}.toString();
    }

    public String updateStockAfterShipment() {
        return new SQL() {{
            UPDATE("stock");
            SET("qty = qty - #{qty}");
            WHERE("stk_id = #{stkId}");
        }}.toString();
    }

    public String selectShipmentById() {
        return new SQL() {{
            SELECT("sh.ship_id, sh.stk_id, sh.req_date, sh.req_qty, sh.qty, sh.status, sh.loc, i.name AS item_name, s.qty AS available_qty");
            FROM("shipment sh");
            INNER_JOIN("stock s ON sh.stk_id = s.stk_id");
            INNER_JOIN("item i ON s.item_id = i.item_id");
            WHERE("sh.ship_id = #{shipId}");
        }}.toString();
    }

    public String checkStockAvailability() {
        return new SQL() {{
            SELECT("qty AS available_qty");
            FROM("stock");
            WHERE("stk_id = #{stkId}");
        }}.toString();
    }
}