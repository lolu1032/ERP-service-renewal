package com.erp.mes.sqlBuilder;

import org.apache.ibatis.jdbc.SQL;
import java.util.Map;

public class StockBuilder {

    // 입고 완료된 자재를 재고로 반영 (입고 수량)
    public String insertStockFromCompletedInput() {
        return new SQL() {{
            INSERT_INTO("stock");
            SELECT("i.item_id, i.qty, i.loc, t.val AS value, i.rec_date AS in_date, " +
                    "DATE_ADD(i.rec_date, INTERVAL 30 DAY) AS exp_date, 'AVAILABLE' AS status");
            FROM("input i");
            JOIN("transaction t ON i.tran_id = t.tran_id");
            WHERE("i.type = true");  // 입고 완료된 항목만 선택 (type이 true인 경우)
        }}.toString();
    }

    // 재고 목록 조회 (발주 품목 등록)
    public String stockList() {
        return new SQL() {{
            SELECT("s.stk_id, i.item_id, i.name AS item_name, i.item_code, " +
                    "s.qty, s.loc, s.value, s.in_date, s.exp_date, " +
                    "s.cons_qty, s.cons_date, s.cons_loc, i.spec, i.price, i.type");
            FROM("stock s");
            JOIN("item i ON s.item_id = i.item_id");
            WHERE("s.cons_qty >= 1");  // 소모 수량(cons_qty)이 1 이상인 항목만 조회
            ORDER_BY("s.stk_id DESC");
        }}.toString();
    }


    // 재고 상세보기 항목 조회
    public String selectStockDetails() {
        return new SQL() {{
            SELECT("s.stk_id, i.item_id, i.name AS item_name, i.item_code, " +
                    "s.qty, s.loc, s.value, s.in_date, s.exp_date, " +
                    "s.cons_qty, s.cons_date, s.cons_loc, i.spec, i.price, i.type");
            FROM("stock s");
            JOIN("item i ON s.item_id = i.item_id");
            WHERE("s.stk_id = #{stkId}"); // 재고 ID로 필터링
        }}.toString();
    }

    public String selectStockList(Map<String, Object> params) {
        return new SQL() {{
            SELECT("s.stk_id, i.name AS item_name, s.qty, s.loc, s.value, s.in_date, s.exp_date, s.status");
            FROM("stock s");
            INNER_JOIN("item i ON s.item_id = i.item_id");

            // 자재명 필터링
            if (params.containsKey("itemName") && params.get("itemName") != null && !params.get("itemName").toString().isEmpty()) {
                WHERE("i.name LIKE CONCAT('%', #{itemName}, '%')");
            }

            // 위치 필터링
            if (params.containsKey("loc") && params.get("loc") != null && !params.get("loc").toString().isEmpty()) {
                WHERE("s.loc LIKE CONCAT('%', #{loc}, '%')");
            }

            // 재고 상태 필터링 (가용재고/비가용재고)
            if (params.containsKey("minQty")) {
                WHERE("s.qty >= #{minQty}");  // 가용재고: 10 이상
            }
            if (params.containsKey("maxQty")) {
                WHERE("s.qty <= #{maxQty}");  // 비가용재고: 10 이하
            }

            // 입고 날짜 필터링
            if (params.containsKey("startDate") && params.get("startDate") != null && params.containsKey("endDate") && params.get("endDate") != null) {
                WHERE("s.in_date BETWEEN #{startDate} AND #{endDate}");
            }

            ORDER_BY("s.stk_id DESC");
        }}.toString();
    }


    // 출고 후 재고 수량 업데이트 및 기록 처리 (출고 수량)
    public String updateStockAfterShipment() {
        return new SQL() {{
            UPDATE("stock");
            SET("qty = qty - #{qty}");
            WHERE("stk_id = #{stkId}");
        }}.toString();
    }

    // 재고 상태 업데이트
    public String updateStockStatus() {
        return new SQL() {{
            UPDATE("stock");
            SET("status = #{status}");
            WHERE("stk_id = #{stkId}");
        }}.toString();
    }


    // 재고 금액 산출 (기간별 재고 금액 산출)
    public String calculateStockValue(final Map<String, Object> params) {
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");

        return new SQL() {{
            SELECT("s.stk_id, i.name AS item_name, s.qty, i.price, " +
                    "(s.qty - IFNULL(shipment_data.shipped_qty, 0)) AS remaining_qty, " +
                    "((s.qty - IFNULL(shipment_data.shipped_qty, 0)) * i.price) AS item_total_value");
            FROM("stock s");
            JOIN("item i ON s.item_id = i.item_id");
            LEFT_OUTER_JOIN("(SELECT sh.stk_id, SUM(sh.qty) AS shipped_qty " +
                    "FROM shipment sh " +
                    "WHERE sh.req_date BETWEEN #{startDate} AND #{endDate} " +
                    "GROUP BY sh.stk_id) shipment_data ON s.stk_id = shipment_data.stk_id");
            WHERE("s.qty - IFNULL(shipment_data.shipped_qty, 0) > 0");
        }}.toString();
    }


    public String getSupplyPrice() {
        return new SQL() {{
            SELECT("s.stk_id, q.price, i.name AS item_name, s.qty, q.quo_id, q.date AS create_date, t.val AS transaction_value, t.date AS transaction_date, t.sup_code");
            FROM("stock s");
            LEFT_OUTER_JOIN("item i ON s.item_id = i.item_id");
            LEFT_OUTER_JOIN("quotation q ON i.item_id = q.item_id");
            LEFT_OUTER_JOIN("transaction t ON t.plan_id = q.quo_id");  // transaction 테이블과 JOIN
            WHERE("s.stk_id = #{stkId}");
            ORDER_BY("q.date DESC");  // 가장 최근의 유효한 견적을 먼저 가져오기 위해
        }}.toString();
    }


    public String selectStockItemList() {
        return new SQL() {{
            SELECT("s.stk_id, i.item_id, i.name AS item_name, i.item_code, " +
                    "s.qty, s.loc, s.value, s.in_date, s.exp_date, " +
                    "s.cons_qty, s.cons_date, s.cons_loc, i.spec, i.price, i.type");
            FROM("stock s");
            JOIN("item i ON s.item_id = i.item_id");
            WHERE("s.qty > 0");  // 재고 수량이 0보다 큰 항목만 조회
            ORDER_BY("s.stk_id DESC");
        }}.toString();
    }

    public String stockItemList() {
        return new SQL() {{
            SELECT("s.stk_id, i.item_id, i.name AS item_name, i.item_code, " +
                    "s.qty, s.loc, s.value, s.in_date, s.exp_date, " +
                    "s.cons_qty, s.cons_date, s.cons_loc, i.spec, i.price, i.type");
            FROM("stock s");
            JOIN("item i ON s.item_id = i.item_id");
            WHERE("s.cons_qty > 0");
        }}.toString();
    }


    public String getStockCalculation(Map<String, Object> params) {
        return new SQL() {{
            SELECT("s.stk_id");
            SELECT("i.name AS item_name");
            SELECT("s.qty AS current_qty");
            SELECT("(" +
                    "SELECT COALESCE(SUM(p.qty), 0) " +
                    "FROM input inp " +
                    "JOIN transaction t ON inp.tran_id = t.tran_id " +
                    "JOIN plan p ON t.plan_id = p.plan_id " +
                    "WHERE inp.type = TRUE " +
                    "AND inp.rec_date BETWEEN #{startDate} AND #{endDate} " +
                    "AND p.item_id = s.item_id" +
                    ") AS incoming_quantity");
            SELECT("COALESCE(SUM(CASE WHEN sh.status = 'COMPLETED' THEN sh.qty ELSE 0 END), 0) AS outgoing_quantity");
            SELECT("MAX(CASE WHEN sh.status = 'COMPLETED' THEN sh.req_date END) AS last_completed_date");
            SELECT("s.loc");
            SELECT("s.value");
            SELECT("s.status");
            // 기존의 s.in_date를 최근 입고 요청 날짜로 변경
            SELECT("(" +
                    "SELECT MAX(inp.rec_date) " +
                    "FROM input inp " +
                    "JOIN transaction t ON inp.tran_id = t.tran_id " +
                    "JOIN plan p ON t.plan_id = p.plan_id " +
                    "WHERE inp.type = TRUE " +
                    "AND inp.rec_date BETWEEN #{startDate} AND #{endDate} " +
                    "AND p.item_id = s.item_id" +
                    ") AS in_date");
            FROM("stock s");
            INNER_JOIN("item i ON s.item_id = i.item_id");
            LEFT_OUTER_JOIN("shipment sh ON s.stk_id = sh.stk_id AND sh.req_date BETWEEN #{startDate} AND #{endDate}");
            WHERE("s.qty > 0");
            GROUP_BY("s.stk_id");
            GROUP_BY("i.name");
            GROUP_BY("s.qty");
            GROUP_BY("s.loc");
            GROUP_BY("s.value");
            GROUP_BY("s.status");

        }}.toString();
    }


}
