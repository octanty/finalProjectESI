package com.example.demo.common.application.exception;

public class PlantNotAvailableException extends Exception {
    private static final long serialVersionUID = 1L;

    public PlantNotAvailableException(String m) {
        super(m);
    }
}
