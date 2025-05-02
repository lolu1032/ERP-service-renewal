
package com.erp.mes.input.controller;

import com.erp.mes.input.domain.InputDTO;
import com.erp.mes.dto.OrderDTO;
import com.erp.mes.dto.PageDTO;
import com.erp.mes.input.service.InputService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 컨트룰러 클래스.
 *
 * 컨트룰러 로직입니다.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/input")
@Slf4j
public class InputController {
    private final InputService service;

    /**
     * n이 0보다 크면 input/inputList 페이지를 보여주고 아니면 401 페이지 반환
     *
     * @param request
     * @return
     */
    @ModelAttribute("servletPath")
    String getRequestServletPath(HttpServletRequest request) {
        return request.getServletPath();
    }
    @PostMapping("/inputList")
    public String inputForm(InputDTO inputDTO) {
        int n = service.inputForm(inputDTO);
        if(n > 0) {
            return "input/inputList";
        }else {
            return "401";
        }
    }
    @PostMapping("/inseception")
    public String inseceptionForm(
            @RequestParam("orderCode") String orderCode,
            @RequestParam("inspectionStatus") String inspectionStatus,
            Model model,
            Map<String,Object> map
    ) {
        log.info("a={} " , inspectionStatus);
        map.put("selectValue",inspectionStatus);
        map.put("orderCode",orderCode);
        service.updateInputStatus(map);
        return "redirect:/input/paging";
    }
    @PostMapping("/search")
    public String search(OrderDTO orderDTO,Model model) {
        List<OrderDTO> list = service.serachList(orderDTO);
        log.info("list={}",list);
        model.addAttribute("list",list);
        return "input/search";
    }
    @PostMapping("/searchTrans")
    public String searchTrans(OrderDTO orderDTO,Model model) {
        List<OrderDTO> list = service.serachListTrans(orderDTO);
        model.addAttribute("list",list);
        return "input/searchTrans";
    }

    @GetMapping("/paging")
    public String paging(Model model,
                         @RequestParam(value = "page", required = false,defaultValue = "1") int page) {
        List<OrderDTO> pagingList = service.pagingList(page);
        PageDTO pageDTO = service.pagingParam(page);
        log.info("pagin={}",pagingList);
        model.addAttribute("boardList",pagingList);
        model.addAttribute("paging",pageDTO);
        return "input/list";
    }
    @GetMapping("/insep")
    public String insep(Model model,
                        @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        List<OrderDTO> pagingList = service.pagingListTrue(page);

        log.info("={}",pagingList);
        log.info("page={}", page);

        PageDTO pageDTO = service.pagingParamTrue(page);
        model.addAttribute("insep",pagingList);
        model.addAttribute("paging", pageDTO);
        return "input/insep";
    }
    @GetMapping("/bom")
    public String bom(Model model) {
        List<OrderDTO> orders = service.selectOrders();
        log.info("orders={}",orders);
        model.addAttribute("orders",orders);
        return "input/bom";
    }
    @GetMapping("/transaction")
    public String transaction(Model model) {
        List<OrderDTO> trans = service.selectTran();
        log.info("trans={}",trans);
        List<OrderDTO> transList = service.selectTranList();
        model.addAttribute("trans",trans);
        model.addAttribute("list",transList);
        return "input/transaction";
    }
    @PostMapping("/transaction")
    public String transactionForm(
            @RequestParam("orderCode") String[] orderCode,
            @RequestParam("transSelect") String[] transSelect,
            Map<String, Object> map) {

        if (orderCode == null || orderCode.length == 0 || transSelect == null || transSelect.length == 0 || orderCode.length != transSelect.length) {
            map.put("error", "잘못된 요청입니다.");
            return "error";
        }

        // 상태와 발주코드를 그룹화
        Map<String, List<String>> groupedByStatus = new HashMap<>();
        for (int i = 0; i < orderCode.length; i++) {
            String code = orderCode[i];
            String status = transSelect[i];

            if (code != null && status != null) {
                groupedByStatus
                        .computeIfAbsent(status, k -> new ArrayList<>())
                        .add(code);
            }
        }

        // 발주마감 상태로 일괄 업데이트 처리
        if (groupedByStatus.containsKey("발주마감")) {
            List<String> codesToUpdate = groupedByStatus.get("발주마감");
            int n = service.updateTrans(codesToUpdate);
            log.info("업데이트된 건수: {}", n);
        }

        return "redirect:/input/transaction";
    }

}