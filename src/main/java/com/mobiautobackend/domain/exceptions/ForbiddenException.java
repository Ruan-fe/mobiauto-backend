package com.mobiautobackend.domain.exceptions;

import com.mobiautobackend.domain.enumeration.ExceptionMessagesEnum;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends HttpException {

    public ForbiddenException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public ForbiddenException(final ExceptionMessagesEnum exceptionMessagesEnum) {
        super(exceptionMessagesEnum.getCode(), exceptionMessagesEnum.getMessage(), HttpStatus.FORBIDDEN);
    }
}