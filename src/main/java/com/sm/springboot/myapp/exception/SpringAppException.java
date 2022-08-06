package com.sm.springboot.myapp.exception;

public class SpringAppException extends RuntimeException {
    public SpringAppException(String exMessage) {
        super(exMessage);
    }
}
