package com.example.demo.invoicing.application.exceptions;

public class InvoiceNotFoundException extends Exception {
    public InvoiceNotFoundException(Long id) {
        super(String.format("Invoice %s not found", id));
    }
}
