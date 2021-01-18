package com.buildit.common.application.exception;

import lombok.Data;

@Data
public class MyError {
    String message;

    public MyError( String message){
        this.message = message;
    }

}
