package com.example.demo.common.application.exception;

public class TaskNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public TaskNotFoundException(String m) {
        super(m);
    }
}