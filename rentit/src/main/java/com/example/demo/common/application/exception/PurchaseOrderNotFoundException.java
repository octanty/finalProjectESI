package com.example.demo.common.application.exception;

public class PurchaseOrderNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public PurchaseOrderNotFoundException(String m) {
                super(m);
    }
}
