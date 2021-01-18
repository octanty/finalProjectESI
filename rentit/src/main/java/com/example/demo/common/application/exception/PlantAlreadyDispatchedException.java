package com.example.demo.common.application.exception;

public class PlantAlreadyDispatchedException extends Exception {
    private static final long serialVersionUID = 1L;

    public PlantAlreadyDispatchedException(String m) { super(m);
    }
}
