package com.erp.mes.input;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    NOT_FOUND_VALUE(HttpStatus.NOT_FOUND,"값이 없다예요!");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status,String message) {
        this.status = status;
        this.message = message;
    }
}
