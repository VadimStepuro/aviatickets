package com.stepuro.aviatickets.api.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotEnoughInfoException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "Not enough info. ";

    public NotEnoughInfoException(){
        super(DEFAULT_MESSAGE);
    }
    public NotEnoughInfoException(String message){
        super(DEFAULT_MESSAGE + message);
    }
}
