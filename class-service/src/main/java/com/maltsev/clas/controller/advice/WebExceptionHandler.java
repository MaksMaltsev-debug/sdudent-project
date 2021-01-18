package com.maltsev.clas.controller.advice;

import com.maltsev.clas.exception.CreateClassException;
import com.maltsev.clas.exception.ReminderNotFoundException;
import com.maltsev.clas.exception.SavePhotoException;
import com.maltsev.clas.exception.UserNotFoundException;
import com.maltsev.clas.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(SavePhotoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse savePhotoException(SavePhotoException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(ClassNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse classNotFoundException(ClassNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(ReminderNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse reminderNotFoundException(ReminderNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse userNotFoundException(UserNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(CreateClassException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse createClassException(CreateClassException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse processValidationError(MethodArgumentNotValidException ex) {
        ErrorResponse response = null;
        BindingResult result = ex.getBindingResult();
        FieldError error = result.getFieldError();
        if (error != null) {
            response = new ErrorResponse(error.getDefaultMessage());
        }
        return response;
    }

    @ExceptionHandler({TransactionSystemException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolation(TransactionSystemException exception) {
        Throwable cause = exception.getRootCause();
        if (cause instanceof ConstraintViolationException) {
            return new ErrorResponse(((ConstraintViolationException) cause).getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList())
                    .get(0));
        }
        return new ErrorResponse(Objects.requireNonNull(cause).getMessage());
    }
}
