package com.stepuro.aviatickets.api.exeptions;

import com.stepuro.aviatickets.api.dto.error.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class ResourceNotFoundExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiError handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
    }

    @ExceptionHandler(NoTicketsLeftException.class)
    public ApiError handleNoTicketsLeftException(NoTicketsLeftException ex){
        return new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
    }

    @ExceptionHandler(NoPathFoundException.class)
    public ApiError handleNoPathException(NoPathFoundException noPathFoundException){
        return new ApiError(HttpStatus.NOT_FOUND, noPathFoundException.getMessage(), noPathFoundException);
    }
}
