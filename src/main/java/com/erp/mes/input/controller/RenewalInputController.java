package com.erp.mes.input.controller;

import com.erp.mes.input.dto.InputCommonDtos.*;
import com.erp.mes.input.service.RenewalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RenewalInputController {

    private final RenewalService service;

    /**
     * 창고 입고 품목에 대해 검수를 수행하고 관련된 데이터(공급자, 창고, 품목, 수량)를 업데이트합니다.
     *
     * @param request 검수 요청에 필요한 공급자명, 창고명, 품목명, 수량, inputId 등을 담은 요청 객체
     * @return 검수 처리 결과에 따른 성공 또는 실패 메시지를 포함한 HTTP 응답
     */
    @PostMapping("inputList")
    public ResponseEntity<String> itemInspec(@RequestBody ItemInspecRequest request) {
       return service.itemInspec(request);
    }

    /**
     * 발주 상태를 업데이트합니다.
     *
     * 이 메소드는 클라이언트로부터 전달받은 `InputStatusRequest` 객체를 기반으로
     * 발주 상태를 업데이트하며, 요청이 성공적으로 처리되면 `성공` 메시지를 반환합니다.
     * 입력값이 누락된 경우 400 상태 코드와 함께 오류 메시지를 반환합니다.
     *
     * @param request 발주 상태 업데이트에 필요한 데이터가 담긴 `InputStatusRequest` 객체
     * @return 상태 업데이트 결과에 따라 성공 메시지 또는 오류 메시지를 포함한 HTTP 응답을 반환합니다.
     */
    @PostMapping("/status")
    public ResponseEntity<String> updateInputStatus(
            @RequestBody InputStatusRequest request
    ) {
        return service.updateInputStatus(request);
    }
}
