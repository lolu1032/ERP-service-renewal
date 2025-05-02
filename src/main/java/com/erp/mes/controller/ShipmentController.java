package com.erp.mes.controller;

import com.erp.mes.dto.ShipmentDTO;
import com.erp.mes.service.ShipmentService;
import com.erp.mes.service.StockService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/shipment")
public class ShipmentController {

    // 출고 요청 생성
    private final ShipmentService shipmentService;
    private final StockService stockService;

    @ModelAttribute("servletPath")
    String getRequestServletPath(HttpServletRequest request) {
        return request.getServletPath();
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("shipmentDTO", new ShipmentDTO());
        model.addAttribute("stockList", stockService.getStockItemList());
        return "shipment/add";
    }

    @PostMapping("/create")
    public String createShipment(@ModelAttribute ShipmentDTO shipmentDTO, Model model, RedirectAttributes redirectAttributes) {
        int result = shipmentService.createShipment(shipmentDTO);

        if (result == -1) {
            model.addAttribute("error", "재고 정보를 찾을 수 없습니다.");
        } else if (result == -2) {
            model.addAttribute("error", "재고 부족: 요청 수량이 현재 재고보다 많습니다.");
        } else if (result > 0) {
            redirectAttributes.addFlashAttribute("message", "출고 요청이 성공적으로 생성되었습니다.");
            return "redirect:/shipment/list";
        } else {
            model.addAttribute("error", "출고 요청 생성에 실패했습니다.");
        }

        model.addAttribute("shipmentDTO", shipmentDTO);
        model.addAttribute("stockList", stockService.getStockItemList());
        return "shipment/add";
    }

    @GetMapping("/list")
    public String listShipments(@RequestParam Map<String, Object> params, Model model) {
        List<ShipmentDTO> shipmentList = shipmentService.getShipmentList(params);
        model.addAttribute("shipmentList", shipmentList);
        return "shipment/list";
    }

    @PostMapping("/complete/{shipId}")
    public String completeShipment(@PathVariable int shipId, @RequestParam int qty, Model model) {
        try {
            shipmentService.completeShipment(shipId, qty);
            return "redirect:/shipment/list";
        } catch (RuntimeException e) {
            model.addAttribute("error", "출고 완료 처리 중 오류가 발생했습니다: " + e.getMessage());
            return "errorPage";
        }
    }

    @PostMapping("/cancel/{shipId}")
    public String cancelShipment(@PathVariable int shipId, Model model) {
        int result = shipmentService.cancelShipment(shipId);
        if (result > 0) {
            return "redirect:/shipment/list";
        } else {
            model.addAttribute("error", "출고 요청 취소에 실패했습니다.");
            return "errorPage";
        }
    }

    @PostMapping("/update-status")
    public String updateShipmentStatus(@RequestParam int shipId,
                                       @RequestParam String status,
                                       Model model) {
        try {
            int result = shipmentService.updateShipmentStatus(shipId, status);
            if (result > 0) {
                return "redirect:/shipment/list";
            } else {
                model.addAttribute("error", "출고 상태 업데이트에 실패했습니다.");
                return "shipment/list";  // errorPage 대신 list 페이지로 이동
            }
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "shipment/list";  // errorPage 대신 list 페이지로 이동
        }
    }

    @GetMapping("/detail/{shipId}")
    public String viewShipmentDetails(@PathVariable("shipId") int shipId, Model model) {
        ShipmentDTO shipment = shipmentService.selectShipmentById(shipId);
        if (shipment == null) {
            model.addAttribute("error", "해당 출고 정보를 찾을 수 없습니다.");
            return "errorPage";
        }
        model.addAttribute("shipment", shipment);
        return "shipment/detail";
    }

    @GetMapping("/search")
    public String searchShipments(@RequestParam(required = false) String itemName,
                                  @RequestParam(required = false) String status,
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reqDate,
                                  Model model) {
        Map<String, Object> params = new HashMap<>();
        if (itemName != null && !itemName.isEmpty()) {
            params.put("itemName", itemName);
        }
        if (status != null && !status.isEmpty()) {
            params.put("status", status);
        }
        if (reqDate != null) {
            params.put("reqDate", reqDate);
        }

        List<ShipmentDTO> shipmentList = shipmentService.searchShipments(params);
        model.addAttribute("shipmentList", shipmentList);
        return "shipment/list";
    }


    @GetMapping("/check-stock/{shipId}")
    public String checkStock(@PathVariable("shipId") int shipId, Model model) {
        ShipmentDTO shipment = shipmentService.checkStockForShipment(shipId);
        model.addAttribute("shipment", shipment);
        return "shipment/stock-check";
    }

    @PostMapping("/partial-complete/{shipId}")
    public String partialCompleteShipment(@PathVariable int shipId, @RequestParam int qty, Model model) {
        try {
            shipmentService.partialCompleteShipment(shipId, qty);
            return "redirect:/shipment/list";
        } catch (RuntimeException e) {
            model.addAttribute("error", "부분 출고 처리 중 오류가 발생했습니다: " + e.getMessage());
            return "errorPage";
        }
    }




    @GetMapping("/reject-reason/{shipId}")
    public String viewRejectReason(@PathVariable("shipId") int shipId, Model model) {
        ShipmentDTO shipment = shipmentService.selectShipmentById(shipId);
        if (shipment == null || !"REJECTED".equals(shipment.getStatus())) {
            model.addAttribute("error", "해당 출고 정보를 찾을 수 없거나 반려 상태가 아닙니다.");
            return "errorPage";
        }
        model.addAttribute("shipment", shipment);
        return "shipment/reject-reason";
    }

}