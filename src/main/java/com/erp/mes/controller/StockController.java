package com.erp.mes.controller;

import com.erp.mes.dto.StockDTO;
import com.erp.mes.service.StockService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
@RequestMapping("/stock")
public class StockController {
    private final StockService stockService;

    @ModelAttribute("servletPath")
    String getRequestServletPath(HttpServletRequest request) {
        return request.getServletPath();
    }

    @GetMapping("/list")
    public String listStock(@RequestParam(required = false) String itemName,
                            @RequestParam(required = false) String stockStatus,  // stockStatus 추가
                            @RequestParam(required = false) String minQty,
                            @RequestParam(required = false) String startDate,
                            @RequestParam(required = false) String endDate,
                            @RequestParam(required = false) String loc,
                            Model model) {

        Map<String, Object> params = new HashMap<>();

        // 검색 조건이 있을 경우에만 파라미터를 Map에 추가
        if (itemName != null && !itemName.isEmpty()) {
            params.put("itemName", itemName);
        }

        // 재고 상태 필터링
        if (stockStatus != null && !stockStatus.isEmpty()) {
            if ("available".equals(stockStatus)) {
                // 가용재고: 10 이상
                params.put("minQty", 10);
            } else if ("unavailable".equals(stockStatus)) {
                // 비가용재고: 10 이하
                params.put("maxQty", 10);  // SQL 쿼리에서 이 값으로 10 이하 필터링
            }
        }

        // 최소 재고 수량 필터링
        if (minQty != null && !minQty.isEmpty()) {
            params.put("minQty", Integer.parseInt(minQty));
        }

        if (startDate != null && !startDate.isEmpty()) {
            params.put("startDate", startDate);
        }
        if (endDate != null && !endDate.isEmpty()) {
            params.put("endDate", endDate);
        }
        if (loc != null && !loc.isEmpty()) {
            params.put("loc", loc);
        }

        // 필터링된 목록을 가져옴
        List<StockDTO> stockList = stockService.getFilteredStockList(params);

        if (stockList != null && !stockList.isEmpty()) {
            model.addAttribute("stockList", stockList);
        } else {
            model.addAttribute("error", "재고 목록을 찾을 수 없습니다.");
        }

        return "stock/list";
    }

    // 재고 상태 업데이트
    @PostMapping("/updatestatus")
    public String updateStockStatus(@RequestParam int stkId, @RequestParam String status, Model model) {
        int result = stockService.updateStockStatus(stkId, status);
        if (result > 0) {
            return "redirect:/stock/list";
        } else {
            model.addAttribute("error", "재고 상태 업데이트에 실패했습니다.");
            return "errorPage";
        }
    }

    @GetMapping("/stockValuePage")
    public String showStockValuePage(Model model) {
        model.addAttribute("pageTitle", "재고 금액 계산 시스템");
        return "stock/stockValuePage";
    }

    @GetMapping("/calculate-value")
    @ResponseBody
    public ResponseEntity<?> calculateStockValue(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        try {
            Map<String, Object> result = stockService.calculateStockValue(startDate, endDate);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace(); // 서버 로그에 에러 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("오류 발생: " + e.getMessage());
        }
    }

    @GetMapping("/detail/{stkId}")
    public String viewStockDetails(@PathVariable("stkId") int stkId, Model model) {
        StockDTO stock = stockService.getStockDetails(stkId);
        if (stock == null) {
            model.addAttribute("error", "해당 재고 정보를 찾을 수 없습니다.");
            return "errorPage"; // 또는 오류 메시지 표시
        }
        model.addAttribute("stock", stock);
        return "stock/detail";
    }

    @GetMapping("/price/{stkId}")
    public String viewPrice(@PathVariable("stkId") int stkId, Model model) {
        List<Map<String, Object>> priceList = stockService.getPrice(stkId);
        System.out.println(priceList); // 디버깅을 위한 로그 출력
        if (priceList != null && !priceList.isEmpty()) {
            model.addAttribute("priceList", priceList);
        } else {
            model.addAttribute("error", "가격 정보를 찾을 수 없습니다.");
        }
        return "stock/price";
    }

    @GetMapping("/calculate")
    public String calculateStock(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Model model) {
        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        List<StockDTO> result = stockService.calculateStock(startDate, endDate);
        model.addAttribute("stockCalculation", result);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "stock/calculation";
    }
}