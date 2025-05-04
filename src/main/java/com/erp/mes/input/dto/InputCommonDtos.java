package com.erp.mes.input.dto;

import lombok.Builder;

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
}
