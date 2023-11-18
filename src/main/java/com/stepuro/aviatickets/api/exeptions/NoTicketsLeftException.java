package com.stepuro.aviatickets.api.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoTicketsLeftException extends RuntimeException {
    public NoTicketsLeftException(){
        super("No tickets left");
    }

    public NoTicketsLeftException(String message){
        super("No tickets left. " + message);
    }
}
