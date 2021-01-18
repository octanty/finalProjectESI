package com.example.demo.common.application.exception;

public class InvalidConditionException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidConditionException(String m) {
        super(m);
    }
}