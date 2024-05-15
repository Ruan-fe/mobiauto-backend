package com.mobiautobackend.domain.exceptions;

import com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum;
import org.springframework.http.HttpStatus;

public class BadRequestException extends HttpException {

    public BadRequestException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(final ExceptionMessagesEnum exceptionMessagesEnum) {
        super(exceptionMessagesEnum.getCode(), exceptionMessagesEnum.getMessage(), HttpStatus.BAD_REQUEST);
    }
}