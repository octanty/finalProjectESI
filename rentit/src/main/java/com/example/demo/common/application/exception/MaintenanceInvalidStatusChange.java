package com.example.demo.common.application.exception;

public class MaintenanceInvalidStatusChange extends Exception {
    private static final long serialVersionUID = 1L;

    public MaintenanceInvalidStatusChange(String m) {
        super(m);
    }
}
