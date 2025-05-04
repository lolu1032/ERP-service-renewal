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
}
