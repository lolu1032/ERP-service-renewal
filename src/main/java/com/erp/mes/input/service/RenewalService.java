package com.erp.mes.input.service;

import com.erp.mes.input.dto.InputCommonDtos.*;
import com.erp.mes.input.repository.InputMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RenewalService {
    private final InputMapper inputMapper;

    /**
     * 품목 입고 검수 요청을 처리합니다.
     *
     * <p>요청된 {@link ItemInspecRequest} 객체의 필수 값들을 검증한 후,
     * 유효한 경우 관련 데이터를 업데이트합니다. 필수 항목이 누락되었을 경우
     * 400 Bad Request를 응답합니다.</p>
     *
     * @param request 품목 검수에 필요한 데이터 (공급업체명, 창고명, 품목명, 수량, 입고 ID)
     * @return 입력값이 유효하면 200 OK 및 "업데이트 되었습니다." 메시지 반환,
     *         유효하지 않으면 400 Bad Request 및 "값을 입력해주세요." 메시지 반환
     */
    public ResponseEntity<String> itemInspec(ItemInspecRequest request) {
        if(request.supName().isEmpty() || request.invenName().isEmpty() || request.itemName().isEmpty() || request.qty() == 0 || request.inputId() == null) {
            return ResponseEntity.badRequest().body("값을 입력해주세요.");
        }
        inputMapper.updateInput(request);
        return ResponseEntity.ok("업데이트 되었습니다.");
    }

}
