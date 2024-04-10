package com.stepuro.aviatickets.api.exeptions;

import com.stepuro.aviatickets.api.dto.error.ApiError;
import com.stepuro.aviatickets.api.dto.error.ApiSubError;
import com.stepuro.aviatickets.api.dto.error.ApiValidationError;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@RestControllerAdvice
public class ValidationHandlers {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiError handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ApiSubError> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            Object rejectedValue = error.getRejectedValue();
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            String objectName = error.getObjectName();

            errors.add(new ApiValidationError(objectName, fieldName, rejectedValue, errorMessage));
        });

        StringBuilder returnMessage = new StringBuilder();
        ex.getAllErrors().forEach((error) -> {
            returnMessage.append(error.getDefaultMessage());
            returnMessage.append(" (");
            returnMessage.append(error.getObjectName());
            returnMessage.append(") ");
        });

        return new ApiError(HttpStatus.BAD_REQUEST, returnMessage.toString(), ex, errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SQLException.class)
    public ApiError handleSqlExceptions(SQLException ex){
        return new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ExpiredJwtException.class)
    public ApiError handleExpiredJwt(ExpiredJwtException ex) {
        return new ApiError(HttpStatus.UNAUTHORIZED, ex.getMessage(), ex);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotEnoughInfoException.class)
    public ApiError handleNotEnoughInfoException(NotEnoughInfoException ex){
        return new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
    }

}