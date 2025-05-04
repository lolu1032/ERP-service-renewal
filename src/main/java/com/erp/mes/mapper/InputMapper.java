//package com.erp.mes.mapper;
//
//import com.erp.mes.input.domain.InputDTO;
//import com.erp.mes.input.domain.OrderDTO;
//import com.erp.mes.input.domain.TransactionDTO;
//import com.erp.mes.sqlBuilder.InputBuilder;
//import org.apache.ibatis.annotations.*;
//
//import java.util.List;
//import java.util.Map;
//
//@Mapper
//public interface InputMapper {
//
//    // 입고수정
//    @UpdateProvider(type = InputBuilder.class, method = "buildUpdateInput")
//    int inputForm(InputDTO inputDTO);
//
//    // 거래 명세서
//    @SelectProvider(type = InputBuilder.class,method = "buildSelectTransaction")
//    List<TransactionDTO> transactionList();
//
//    // 거래명세서 수정
//    @UpdateProvider(type = InputBuilder.class, method = "buildUpdateTransaction")
//    int transactionForm(TransactionDTO transactionDTO);
//    // 발주서 넣기
////    @InsertProvider(type = InputBuilder.class,method = "buildInsertOrder")
////    int insertOrder(OrderDTO orderDTO);
//    // 구매발주서 리스트
//    @SelectProvider(type = InputBuilder.class, method = "buildSelectOrder")
//    List<OrderDTO> orderList();
//
////    // 구매발주서 확인
////    @UpdateProvider(type = InputBuilder.class, method = "buildUpdateOrder")
////    int orderForm(Map<String,Object> map);
//
//    // 검수
//    //    update문 where status = 100형식으로 다가가야할듯
//    @UpdateProvider(type = InputBuilder.class, method = "buildUpdateInputStatus")
//    int updateInputStatus(Map<String,Object> map);
//
//    // 검색
//    @SelectProvider(type = InputBuilder.class, method = "buildSearch")
//    List<OrderDTO> serachList(OrderDTO orderDTO);
//    @SelectProvider(type = InputBuilder.class, method = "buildSearchTrans")
//    List<OrderDTO> serachListTrans(OrderDTO orderDTO);
//
//    @SelectProvider(type = InputBuilder.class, method = "buildPaging")
//    List<OrderDTO> pagingList(Map<String, Object> map );
//    @SelectProvider(type = InputBuilder.class, method = "buildPagingTrue")
//    List<OrderDTO> pagingListTrue(Map<String, Object> map );
//    @SelectProvider(type = InputBuilder.class, method = "buildPageCountTrue")
//    int boardCountTrue();
//
//    @SelectProvider(type = InputBuilder.class, method = "buildPageCount")
//    int boardCount();
//
//    @SelectProvider(type = InputBuilder.class, method = "buildSelectOrders")
//    List<OrderDTO> selectOrders();
//    @SelectProvider(type = InputBuilder.class , method = "buildSelectTrans")
//    List<OrderDTO> selectTrans();
//    @SelectProvider(type = InputBuilder.class , method = "buildSelectTransList")
//    List<OrderDTO> selectTransList();
//    @UpdateProvider(type = InputBuilder.class, method = "buildUpdateTrans")
//    int updateTrans(Map<String,Object> param);
//}