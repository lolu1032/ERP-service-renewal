package com.erp.mes.input.service;

import com.erp.mes.input.domain.InputDTO;
import com.erp.mes.dto.OrderDTO;
import com.erp.mes.dto.PageDTO;
import com.erp.mes.dto.TransactionDTO;
import com.erp.mes.mapper.InputMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InputService {
    private final InputMapper mapper;
    public int inputForm(InputDTO inputDTO) {
        return mapper.inputForm(inputDTO);
    }
    public List<TransactionDTO> transactionList() {
        return mapper.transactionList();
    }
    public int transactionFrom(TransactionDTO transactionDTO) {
        return mapper.transactionForm(transactionDTO);
    }
    //    public int insertOrder(OrderDTO orderDTO){
//        return mapper.insertOrder(orderDTO);
//    }
    public List<OrderDTO> orderList() {
        return mapper.orderList();
    }
    //    public int orderForm(Map<String,Object> map) {
//        return mapper.orderForm(map);
//    }
    public int updateInputStatus(Map<String,Object> map) {
        return mapper.updateInputStatus(map);
    }

    public List<OrderDTO> serachList(OrderDTO orderDTO){return mapper.serachList(orderDTO);}
    public List<OrderDTO> serachListTrans(OrderDTO orderDTO){return mapper.serachListTrans(orderDTO);}

    public List<OrderDTO> selectOrders(){
        return mapper.selectOrders();
    }

    public List<OrderDTO> selectTran() {return mapper.selectTrans();}
    public List<OrderDTO> selectTranList() {return mapper.selectTransList();}
    public int updateTrans(List<String> orderCodes) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderCodes", orderCodes);

        return mapper.updateTrans(params);
    }

    private static int pageLimit = 10; // 한페이지당 보여줄 글 갯수
    private static int blockLimit = 10; // 하단에 보여줄 페이지 번호 수
    public List<OrderDTO> pagingList(int page) {
        int pagingStart = (page - 1 ) * pageLimit;
        Map<String,Object> pagingParams = new HashMap<>();
        pagingParams.put("start", pagingStart);
        pagingParams.put("limit",pageLimit);
        List<OrderDTO> pagingList = mapper.pagingList(pagingParams);
        return pagingList;
    }
    public List<OrderDTO> pagingListTrue(int page) {
        int pagingStart = (page - 1 ) * pageLimit;
        Map<String,Object> pagingParams = new HashMap<>();
        pagingParams.put("start", pagingStart);
        pagingParams.put("limit",pageLimit);
        List<OrderDTO> pagingList = mapper.pagingListTrue(pagingParams);
        return pagingList;
    }

    public PageDTO pagingParam(int page) {
        // 전체 글 갯수 조회
        int boardCount = mapper.boardCount();
        // 전체 페이지 갯수 계산(10/3=3.33333 => 4)
        int maxPage = (int) (Math.ceil((double) boardCount / pageLimit));
        // 시작 페이지 값 계산(1, 4, 7, 10, ~~~~)
        int startPage = (((int)(Math.ceil((double) page / blockLimit))) - 1) * blockLimit + 1;
        // 끝 페이지 값 계산(3, 6, 9, 12, ~~~~)
        int endPage = startPage + blockLimit - 1;
        if (endPage > maxPage) {
            endPage = maxPage;
        }
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setMaxPage(maxPage);
        pageDTO.setStartPage(startPage);
        pageDTO.setEndPage(endPage);
        return pageDTO;
    }
    public PageDTO pagingParamTrue(int page) {
        int filteredBoardCount = mapper.boardCountTrue(); // 필터링된 데이터 개수 조회
        // 전체 페이지 개수 계산
        int maxPage = (int) (Math.ceil((double) filteredBoardCount / pageLimit));
        int startPage = (((int) (Math.ceil((double) page / blockLimit))) - 1) * blockLimit + 1;
        int endPage = startPage + blockLimit - 1;
        if (endPage > maxPage) {
            endPage = maxPage;
        }
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setMaxPage(maxPage);
        pageDTO.setStartPage(startPage);
        pageDTO.setEndPage(endPage);
        return pageDTO;
    }
}