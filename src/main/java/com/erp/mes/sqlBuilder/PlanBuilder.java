package com.erp.mes.sqlBuilder;

import org.apache.ibatis.jdbc.SQL;

public class PlanBuilder {

    public String buildSelectPlan() {
        return new SQL(){{
            SELECT("plan.date, plan.qty, plan.leadtime, plan.status, item.name AS item_name, item.price, item.item_id");
            FROM("plan");
            JOIN("item ON item.item_id = plan.item_id");
        }}.toString();
    }
    public String buildInsertPlan() {
        return new SQL(){{
            INSERT_INTO("plan");
            VALUES("date"," #{date}");
            VALUES("item_id","#{itemId}");
            VALUES("leadtime","#{leadtime}");
            VALUES("qty","#{qty}");
            VALUES("status","#{status}");
        }}.toString();
    }
}
