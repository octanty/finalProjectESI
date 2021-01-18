package com.example.demo.common.application.exception;

public class MaintenanceAlreadyStartedException extends Exception {
    private static final long serialVersionUID = 1L;

    public MaintenanceAlreadyStartedException(String m) {
        super(m);
    }
}