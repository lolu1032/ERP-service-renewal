package com.erp.mes.input.service;

import com.erp.mes.input.domain.OrderDTO;
import com.erp.mes.input.dto.InputCommonDtos.*;
import com.erp.mes.input.repository.InputMapper;
import jakarta.xml.ws.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 발주 상태를 실제로 업데이트하는 서비스 메소드입니다.
     *
     * 이 메소드는 `InputStatusRequest` 객체에서 발주 상태를 처리하고,
     * 상태값이 `'완료'`인지 확인한 뒤 해당 상태를 데이터베이스에 업데이트합니다.
     *
     * 상태값이 `'완료'`일 경우 "발주 완료 되었습니다." 메시지를 반환하며,
     * `'완료'`가 아닐 경우 "발주 미완료" 메시지를 반환합니다.
     *
     * 만약 입력값 중 `orderCode` 또는 `selectValue`가 비어 있으면,
     * `400 Bad Request` 응답을 반환하고 오류 메시지를 전달합니다.
     *
     * @param request 발주 상태 업데이트에 필요한 데이터가 담긴 `InputStatusRequest` 객체
     * @return 상태 업데이트 성공 시 적절한 메시지와 함께 200 OK 응답을 반환합니다.
     *         입력값이 비어 있으면 400 Bad Request와 함께 오류 메시지를 반환합니다.
     */
    public ResponseEntity<String> updateInputStatus(InputStatusRequest request) {
        String message;

        if(request.orderCode().isEmpty() || request.selectValue().isEmpty()) {
            return ResponseEntity.badRequest().body("값을 입력해주세요.");
        }
        inputMapper.updateInputStatus(request);

        if(!request.selectValue().equals("완료")) {
            message = "발주 미완료";
        }else {
            message = "발주 완료 되었습니다.";
        }

        return ResponseEntity.ok(message);
    }

    /**
     * 키워드를 기반으로 주문 정보를 검색하고, 결과 여부에 따라 응답 메시지를 반환합니다.
     *
     * @param keyword 검색할 키워드 (공급업체명, 주문 코드, 품목명 등)
     * @return 검색 결과가 존재하면 "검색완료" 메시지를 포함한 200 OK 응답,
     *         결과가 없으면 "검색결과가 없습니다." 메시지를 포함한 400 Bad Request 응답
     */
    public ResponseEntity<String> search(String keyword) {
        List<OrderDTO> list = inputMapper.searchInput(keyword);
        if(list.isEmpty()) {
            return ResponseEntity.badRequest().body("검색결과가 없습니다.");
        }
        return ResponseEntity.ok("검색완료");
    }

    /**
     * 주문 코드 리스트를 받아 상태를 일괄적으로 '발주마감'으로 업데이트합니다.
     *
     * @param list 클라이언트에서 전달된 OrderCode 객체 리스트
     * @return 처리 결과에 따른 HTTP 응답. 실패 시 400, 성공 시 200 반환
     */
    public ResponseEntity<String> updateOrdering(List<OrderCode> list) {
        if(list.get(0).orderCode().isEmpty()) {
            return ResponseEntity.badRequest().body("값 입력을 안했습니다.");
        }

        inputMapper.updateTrans(list);

        return ResponseEntity.ok("발주마감 업데이트 되었습니다.");
    }
}
