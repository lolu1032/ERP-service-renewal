package com.erp.mes.restController;

import com.erp.mes.dto.EmailDTO;
import com.erp.mes.dto.PlanDTO;
import com.erp.mes.dto.SupplierDTO;
import com.erp.mes.service.MailService;
import com.erp.mes.service.PurchaseService;
import com.erp.mes.service.QuotationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PurchaseRestController {

    private final QuotationService quotationService;

    private final PurchaseService purchaseService;

    private final MailService mailService;

    public PurchaseRestController(QuotationService quotationService, PurchaseService purchaseService, MailService mailService) {
        this.quotationService = quotationService;
        this.purchaseService = purchaseService;
        this.mailService = mailService;
    }

    //    @GetMapping(value = "purchase/plan")
    //    public String getPlan(){
    //        return "purchase/plan";
    //    }

    /**
     * 조달계획 리스트 조회
     * - 조회 기준 정하기
     *
     * @param
     * @return
     */
    @PostMapping(value = "purchase/plan")
    public Map<String, Object> plan() {
        Map<String, Object> map = new HashMap<>();
        List<PlanDTO> planList = purchaseService.plan();
        map.put("planList", planList);
        return map;
    }


    // 발주서 발행
    @PostMapping("purchase/orderCreate")
    public Map<String, Object> orderCreate(@RequestBody Map<String, Object> map) {
        int result = purchaseService.orderCreate(map);
        map.put("result", result);
        return map;
    }

    // 메일 발송
    @PostMapping(value = "purchase/sendEmail")
    public Map<String, Object> sendEmail(@RequestBody Map<String, Object> map) {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setTargetMail(((String) map.get("targetMail")));
        if (mailService.sendMail(emailDTO)) {
            map.put("msg", "메일발송 성공");
        } else {
            map.put("result", "메일발송 실패");
        }
        return map;
    }

    /**
     * 검수 생성
     *
     * @param map
     * @return
     */
    @PostMapping(value = "purchase/addInspec")
    public Map<String, Object> addInspec(Map<String, Object> map) {
        int result = purchaseService.addInspec(map);
        map.put("msg","검수 등록 성공");
        return map;
    }

    /**
     * 검수 수정
     *
     * @param map
     * @return
     */
    @PostMapping(value = "purchase/inspectionUpdate")
    public Map<String, Object> inspectionUpdate(@RequestBody Map<String, Object> map) {

        System.out.println(" ORDER ID : " + map.get("orderId"));
        System.out.println(" ORDER_CODE : " + map.get("orderCode"));
        System.out.println(" STATUS : " + map.get("status"));

        int result = purchaseService.inspectionUpdate(map);

        return map;
    }

//    @PostMapping(value = "purchase/getAllSupplier")
//    public Map<String, Object> getSupplier(Map<String, Object> map) {
//        List<SupplierDTO> supList = purchaseService.getSupplier();
//        map.put("supList",supList);
//        return map;
//    }

    @PostMapping(value = "purchase/quoCreate")
    public Map<String,Object> quoCreate(Map<String,Object> map){
        int status = quotationService.quoCreate(map);
        map.put("status",status);
        return map;
    }

}
