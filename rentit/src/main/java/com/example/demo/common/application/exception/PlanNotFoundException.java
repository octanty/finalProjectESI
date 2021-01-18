package com.example.demo.common.application.exception;

public class PlanNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public PlanNotFoundException(String m) {
                super(m);
    }
}