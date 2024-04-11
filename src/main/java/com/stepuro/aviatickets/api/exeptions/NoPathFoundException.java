package com.stepuro.aviatickets.api.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoPathFoundException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "No path found. ";

    public NoPathFoundException() {super(DEFAULT_MESSAGE);}

    public NoPathFoundException(String message){super(DEFAULT_MESSAGE + message);}
}
