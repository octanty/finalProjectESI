package com.buildit.common.application.exception;

public class PhrStatusIsNotPending extends Exception {
    private static final long serialVersionUID = 1L;

    public PhrStatusIsNotPending(String m) {
        super(m);
    }
}
