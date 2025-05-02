package com.erp.mes.mapper;

import com.erp.mes.dto.ShipmentDTO;
import com.erp.mes.sqlBuilder.ShipmentBuilder;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface ShipmentMapper {

    @InsertProvider(type = ShipmentBuilder.class, method = "insertShipment")
    int insertShipment(ShipmentDTO shipmentDTO);

    @SelectProvider(type = ShipmentBuilder.class, method = "selectShipmentList")
    @Results({
            @Result(property = "shipId", column = "ship_id"),
            @Result(property = "stkId", column = "stk_id"),
            @Result(property = "reqDate", column = "req_date"),
            @Result(property = "reqQty", column = "req_qty"),
            @Result(property = "qty", column = "qty"),
            @Result(property = "status", column = "status"),
            @Result(property = "loc", column = "loc"),
            @Result(property = "itemName", column = "item_name"),
            @Result(property = "availableQty", column = "available_qty")
    })
    List<ShipmentDTO> selectShipmentList(Map<String, Object> params);

    @UpdateProvider(type = ShipmentBuilder.class, method = "updateShipmentStatusToCompleted")
    int updateShipmentStatusToCompleted(@Param("shipId") int shipId, @Param("qty") int qty);

    @UpdateProvider(type = ShipmentBuilder.class, method = "cancelShipment")
    int cancelShipment(@Param("shipId") int shipId);


    @UpdateProvider(type = ShipmentBuilder.class, method = "updateStockAfterShipment")
    int updateStockAfterShipment(@Param("stkId") int stkId, @Param("qty") int qty);

    @SelectProvider(type = ShipmentBuilder.class, method = "selectShipmentById")
    @Results({
            @Result(property = "shipId", column = "ship_id"),
            @Result(property = "stkId", column = "stk_id"),
            @Result(property = "reqDate", column = "req_date"),
            @Result(property = "reqQty", column = "req_qty"),
            @Result(property = "qty", column = "qty"),
            @Result(property = "status", column = "status"),
            @Result(property = "loc", column = "loc"),
            @Result(property = "itemName", column = "item_name"),
            @Result(property = "availableQty", column = "available_qty")
    })
    ShipmentDTO selectShipmentById(@Param("shipId") int shipId);

    @SelectProvider(type = ShipmentBuilder.class, method = "checkStockAvailability")
    Map<String, Object> checkStockAvailability(@Param("stkId") int stkId);
    // 새로 추가된 메서드

    @Select("SELECT qty FROM stock WHERE stk_id = #{stkId}")
    int getAvailableStock(@Param("stkId") int stkId);

    @Update("UPDATE shipment SET qty = #{qty} WHERE ship_id = #{shipId}")
    int updateShipmentQuantity(@Param("shipId") int shipId, @Param("qty") int qty);
    @Update("UPDATE shipment SET status = #{status} WHERE ship_id = #{shipId}")
    int updateShipmentStatus(@Param("shipId") int shipId, @Param("status") String status);

}