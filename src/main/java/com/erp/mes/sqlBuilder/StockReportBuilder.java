package com.erp.mes.sqlBuilder;

import org.apache.ibatis.jdbc.SQL;
import java.util.Map;

public class StockReportBuilder {
    public String generateStockReport(Map<String, Object> params) {
        return new SQL() {{
            SELECT("s.stk_id, i.item_id, i.name AS itemName, s.qty AS totalQty, " +
                    "s.loc AS location, s.exp_date AS expirationDate, s.qty AS remainingQty, " +
                    "q.price AS unitPrice, (s.qty * q.price) AS totalValue, s.in_date AS date");
            FROM("stock s");
            INNER_JOIN("item i ON s.item_id = i.item_id");
            LEFT_OUTER_JOIN("quotation q ON i.item_id = q.item_id");

            if (params.containsKey("startDate") && params.containsKey("endDate")) {
                WHERE("s.in_date BETWEEN #{startDate} AND #{endDate}");
            }
            if (params.containsKey("itemId") && params.get("itemId") != null) {
                AND().WHERE("i.item_id = #{itemId}");
            }

            ORDER_BY("s.in_date DESC");
        }}.toString();
    }

    public String calculateTotalValue(Map<String, Object> params) {
        return new SQL() {{
            SELECT("SUM(s.qty * q.price) AS totalValue");
            FROM("stock s");
            INNER_JOIN("item i ON s.item_id = i.item_id");
            LEFT_OUTER_JOIN("quotation q ON i.item_id = q.item_id");

            if (params.containsKey("startDate") && params.containsKey("endDate")) {
                WHERE("s.in_date BETWEEN #{startDate} AND #{endDate}");
            }
            if (params.containsKey("itemId") && params.get("itemId") != null) {
                AND().WHERE("i.item_id = #{itemId}");
            }
        }}.toString();
    }

    public String selectStockDetails() {
        return new SQL() {{
            SELECT("s.stk_id, i.item_id, i.name AS itemName, i.item_code, s.qty AS totalQty, " +
                    "s.loc AS location, s.exp_date AS expirationDate, s.qty AS remainingQty, " +
                    "q.price AS unitPrice, (s.qty * q.price) AS totalValue, s.in_date AS date, " +
                    "s.value, s.cons_qty, s.cons_date, s.cons_loc");
            FROM("stock s");
            INNER_JOIN("item i ON s.item_id = i.item_id");
            LEFT_OUTER_JOIN("quotation q ON i.item_id = q.item_id");
            WHERE("s.stk_id = #{stkId}");
            LIMIT(1);  // LIMIT 1 추가
        }}.toString();
    }

}