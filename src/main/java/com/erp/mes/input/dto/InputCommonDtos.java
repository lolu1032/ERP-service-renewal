package com.erp.mes.input.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;

public final class InputCommonDtos {
    @Builder
    public record ItemInspecRequest(
            String supName,
            String invenName,
            String itemName,
            int qty,
            Long inputId
    ) {}

    @Builder
    public record  InputStatusRequest(
            String orderCode,
            String selectValue
    ){}

    @Builder
    public record OrderCode(
            String orderCode
    ) {}

    @Builder
    public record ApiResponse(
            String message,
            HttpStatus status
    ){}
}
