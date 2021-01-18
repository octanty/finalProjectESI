package com.example.demo.common.application.exception;

public class PlantNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public PlantNotFoundException(String m) {
        super(m);
    }
}