package com.example.user_web_service.exception;

import com.example.user_web_service.payload.response.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@CrossOrigin
@RestControllerAdvice
@Log4j2
public class CustomExceptionHandler  extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerDuplicateException(Exception e) {
        return new ErrorResponse(new Date(), HttpStatus.CONFLICT.toString(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerException(Exception e, WebRequest request) {
        e.printStackTrace();
        return new ErrorResponse(new Date(), HttpStatus.BAD_REQUEST.toString(), e.getMessage());
    }
    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        logger.error(ex.getMessage());
        return new ErrorResponse(new Date(), HttpStatus.NOT_FOUND.toString(), ex.getMessage());
    }

}
