package com.erp.mes.sqlBuilder;

import com.erp.mes.input.domain.InputDTO;
import com.erp.mes.input.domain.OrderDTO;
import com.erp.mes.input.domain.TransactionDTO;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public class InputBuilder {
    private static final String MAX_NUMBER = "100";
    private static final Integer MIN_NUMBER =0;
    public String buildUpdateInput(InputDTO inputDTO) {
        return new SQL() {{
            UPDATE("input");
            JOIN("transaction on transaction.tran_id = `input`.tran_id");
            JOIN("supplier on supplier.sup_code = transaction.sup_code");
            JOIN("plan on plan.plan_id = transaction.plan_id");
            JOIN("item on item.item_id = plan.item_id");
            JOIN("inventory on inventory.inven_id = supplier.inven_id");
            if(inputDTO.getSupName() != null) {
                SET("supplier.name = #{supName}");
            }
            if(inputDTO.getInvenName() != null) {
                SET("inventory.name = #{invenName}");
            }
            if(inputDTO.getItemName() != null) {
                SET("item.name = #{itemName}");
            }
            if(inputDTO.getQty()!= MIN_NUMBER) {
                SET("plan.qty = #{qty}");
            }
            WHERE("input.input_id = #{inputId}");
        }}.toString();
    }
    // 거래명세서 조회
    public String buildSelectTransaction() {
        return new SQL(){{
            SELECT("transaction.date, transaction.val,`input`.exp_date,plan.qty,item.name,item.price,item.unit");
            FROM("transaction");
            JOIN("plan on plan.plan_id = transaction.plan_id");
            JOIN("`input` on `input`.tran_id = transaction.tran_id");
            JOIN("item on item.item_id = plan.item_id");
        }}.toString();
    }
    // 거래명세서 업데이트
    public String buildUpdateTransaction(TransactionDTO transactionDTO){
        return new SQL(){{
            UPDATE("transaction");
            JOIN("plan on plan.plan_id = transaction.plan_id");
            JOIN("input on input.tran_id = transaction.tran_id");
            JOIN("item on item.item_id = plan.item_id");

            if (transactionDTO.getDate() != null) {
                SET("transaction.date = #{date}");
            }
            if (transactionDTO.getExpDate() != null) {
                SET("input.exp_date = #{expDate}");
            }
            if(transactionDTO.getQty() != MIN_NUMBER) {
                SET("plan.qty = #{qty}");
            }

            if(transactionDTO.getItemName() != null) {
                SET("item.name = #{itemName}");
                SET("item.price = #{price}");
                SET("item.unit = #{unit}");
                SET("transaction.val = #{price} * #{qty}");
            }
            WHERE("transaction.tran_id = #{tranId}");
        }}.toString();
    }

    // 구매발주서 조회
    public String buildSelectOrder() {
        return new SQL(){{
            SELECT("order.date, order.leadtime,order.status,order.value,item.name,item.price,item.unit");
            FROM("order");
            JOIN("item on item.item_id = `order`.item_id");
        }}.toString();
    }

    // 구매발주서 검수
    public String buildUpdateInputStatus(Map<String,Object> map) {
        return new SQL() {{
            if(map.get("selectValue").equals("완료")) {
                UPDATE("`order`");
                SET("order.insep_status = 1");
                SET("order.status = '발주완료'");
                WHERE("order_code = #{orderCode}");
            }else {
                UPDATE("`order`");
                SET("order.insep_status = 0");
                WHERE("order_code = #{orderCode}");
            }
        }}.toString();
    }
    // 입고 조회 리스트
    public String buildPaging(Map<String,Object> map) {
        return new SQL(){{
            SELECT("order.order_code as orderCode, item.name as itemName, plan.qty, plan.leadtime, order.status,supplier.name AS supName, order.value");
            FROM("`order`");
            JOIN("supplier ON supplier.sup_id = order.sup_id");
            JOIN("plan ON plan.plan_id = order.plan_id");
            JOIN("item ON item.item_id = plan.item_id");
            ORDER_BY("order_id desc");
            LIMIT("#{start} , #{limit}");
            WHERE("order.insep_status = 0");
        }}.toString();
    }
    // 입고조회 페이징
    public String buildPageCount() {
        return new SQL(){{
            SELECT("count(input_id)");
            FROM("`input`");
            WHERE("input.type=false");
        }}.toString();
    }
    // 검수조회 페이지
    public String buildPagingTrue(Map<String,Object> map) {
        return new SQL(){{
            SELECT("order.order_code as orderCode, item.name as itemName, plan.qty, plan.leadtime, order.status,supplier.name AS supName, order.value");
            FROM("`order`");
            JOIN("supplier ON supplier.sup_id = order.sup_id");
            JOIN("plan ON plan.plan_id = order.plan_id");
            JOIN("item ON item.item_id = plan.item_id");
            ORDER_BY("order_id desc");
            LIMIT("#{start} , #{limit}");
            WHERE("order.insep_status = 1");
        }}.toString();
    }
    // 입고 검색
    public String buildSearch(OrderDTO orderDTO) {
        return new SQL(){{
            SELECT("order.order_code as orderCode, item.name as itemName, plan.qty, plan.leadtime, order.status,supplier.name AS supName, order.value");
            FROM("`order`");
            JOIN("supplier ON supplier.sup_id = order.sup_id");
            JOIN("plan ON plan.plan_id = order.plan_id");
            JOIN("item ON item.item_id = plan.item_id");
            if(orderDTO.getKeyword() != null) {
                WHERE("supplier.name like concat('%', #{keyword}, '%')");
                AND();
                WHERE("order.insep_status = false");
                OR();
                WHERE("order.order_code like concat('%', #{keyword}, '%')");
                AND();
                WHERE("order.insep_status = false");
                OR();
                WHERE("item.name like concat('%', #{keyword}, '%')");
                AND();
                WHERE("order.insep_status = false");
            }
        }}.toString();
    }
    // 검수 페이징
    public String buildPageCountTrue() {
        return new SQL(){{
            SELECT("count(*)");
            FROM("`input`");
            WHERE("input.type = true");
        }}.toString();
    }
    // 구매발주서 조회
    public String buildSelectOrders(Map<String, Object> params) {
        return new SQL() {{
            SELECT("order.order_code as orderCode, item.name as itemName, plan.qty, plan.leadtime, order.status,supplier.name AS supName, order.value");
            FROM("`order`");
            JOIN("supplier ON supplier.sup_id = order.sup_id");
            JOIN("plan ON plan.plan_id = order.plan_id");
            JOIN("item ON item.item_id = plan.item_id");
            WHERE("order.status = '발주완료'");
        }}.toString();
    }
    // 거래명세서 조회
    public String buildSelectTrans(Map<String, Object> params) {
        return new SQL() {{
            SELECT("order.order_code as orderCode, item.name as itemName, plan.qty, plan.leadtime, order.status,supplier.name AS supName, order.value");
            FROM("`order`");
            JOIN("supplier ON supplier.sup_id = order.sup_id");
            JOIN("plan ON plan.plan_id = order.plan_id");
            JOIN("item ON item.item_id = plan.item_id");
            WHERE("order.status = '발주완료'");
        }}.toString();
    }

    public String buildSelectTransList(Map<String, Object> params) {
        return new SQL() {{
            SELECT("order.order_code as orderCode, item.name as itemName, plan.qty, plan.leadtime, order.status,supplier.name AS supName, order.value");
            FROM("`order`");
            JOIN("supplier ON supplier.sup_id = order.sup_id");
            JOIN("plan ON plan.plan_id = order.plan_id");
            JOIN("item ON item.item_id = plan.item_id");
            WHERE("order.status = '발주마감'");
        }}.toString();
    }
    // 거래마감
    public String buildUpdateTrans(Map<String, Object> params) {
        List<String> orderCodes = (List<String>) params.get("orderCodes");
        StringBuilder inClause = new StringBuilder();
        for (int i = 0; i < orderCodes.size(); i++) {
            inClause.append("#{orderCodes[").append(i).append("]}");
            if (i < orderCodes.size() - 1) {
                inClause.append(", ");
            }
        }

        return new SQL() {{
            UPDATE("`order`");
            SET("status = '발주마감'");
            WHERE("order_code IN (" + inClause.toString() + ")");
        }}.toString();
    }
    // 거래 검색
    public String buildSearchTrans(OrderDTO orderDTO) {
        return new SQL(){{
            SELECT("order.order_code as orderCode, item.name as itemName, plan.qty, plan.leadtime, order.status,supplier.name AS supName, order.value");
            FROM("`order`");
            JOIN("supplier ON supplier.sup_id = order.sup_id");
            JOIN("plan ON plan.plan_id = order.plan_id");
            JOIN("item ON item.item_id = plan.item_id");
            if(orderDTO.getKeyword() != null) {
                WHERE("supplier.name like concat('%', #{keyword}, '%')");
                OR();
                WHERE("order.order_code like concat('%', #{keyword}, '%')");
                OR();
                WHERE("item.name like concat('%', #{keyword}, '%')");
            }
        }}.toString();
    }

}