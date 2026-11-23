package com.fag.musicalmodel.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private final HttpStatus status;

    public BusinessException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public static BusinessException conflito(String message) {
        return new BusinessException(HttpStatus.CONFLICT, message);
    }

    public static BusinessException requisicaoInvalida(String message) {
        return new BusinessException(HttpStatus.BAD_REQUEST, message);
    }
}
