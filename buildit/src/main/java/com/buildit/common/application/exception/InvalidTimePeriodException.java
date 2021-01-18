package com.buildit.common.application.exception;




public class InvalidTimePeriodException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidTimePeriodException(String m) {
        super(m);
    }
}