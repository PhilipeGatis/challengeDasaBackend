package com.philipe.dasa.labor.labormanager.web.advice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.RollbackException;
import javax.validation.ConstraintViolationException;

import com.philipe.dasa.labor.labormanager.web.response.ErrorResponse;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public final ResponseEntity<ErrorResponse> handleException(Throwable ex, WebRequest request) {
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
            return handleThrowable(status);
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            HttpStatus status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
            return handleThrowable(status);
        } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
            HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
            return handleThrowable(status);
        } else if (ex instanceof MissingPathVariableException) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleThrowable(status);
        } else if (ex instanceof MissingServletRequestParameterException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleThrowable(status);
        } else if (ex instanceof ServletRequestBindingException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleThrowable(status);
        } else if (ex instanceof ConversionNotSupportedException) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleThrowable(status);
        } else if (ex instanceof TypeMismatchException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleThrowable(status);
        } else if (ex instanceof HttpMessageNotReadableException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleThrowable(status);
        } else if (ex instanceof HttpMessageNotWritableException) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleThrowable(status);
        } else if (ex instanceof MethodArgumentNotValidException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleMethodArgumentNotValidException(status, (MethodArgumentNotValidException) ex);
        } else if (ex instanceof MissingServletRequestPartException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleThrowable(status);
        } else if (ex instanceof BindException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleThrowable(status);
        } else if (ex instanceof NoHandlerFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            return handleThrowable(status);
        } else if (ex instanceof AsyncRequestTimeoutException) {
            HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
            return handleThrowable(status);
        } else if (ex instanceof ConstraintViolationException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleConstraintViolationException(status, (ConstraintViolationException) ex);
        } else if (ex instanceof ResponseStatusException) {
            return handleThrowable(((ResponseStatusException) ex).getStatus());
        } else if (ex instanceof TransactionSystemException) {
            HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
            ResponseEntity<ErrorResponse> errorEntity = handleException(ex.getCause(), request);
            ErrorResponse error =
                    ErrorResponse.builder()
                            .statusCode(status.value())
                            .statusMessage(status.getReasonPhrase())
                            .errors(errorEntity.getBody().getErrors())
                            .build();
            return ResponseEntity.status(status).body(error);
        } else if (ex instanceof RollbackException) {
            HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
            ResponseEntity<ErrorResponse> errorEntity = handleException(ex.getCause(), request);
            ErrorResponse error =
                    ErrorResponse.builder()
                            .statusCode(status.value())
                            .statusMessage(status.getReasonPhrase())
                            .errors(errorEntity.getBody().getErrors())
                            .build();
            return ResponseEntity.status(status).body(error);
        } else {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleThrowable(status);
        }
    }

    private ResponseEntity<ErrorResponse> handleThrowable(HttpStatus status) {
        Map<String, Object> errors = new HashMap<>();

        ErrorResponse response =
                ErrorResponse.builder()
                        .statusCode(status.value())
                        .statusMessage(status.getReasonPhrase())
                        .errors(Arrays.asList(errors))
                        .build();

        return ResponseEntity.status(status.value()).body(response);
    }

    private ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            HttpStatus status, MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();

        ex.getBindingResult()
                .getAllErrors()
                .forEach(
                        (error) -> {
                            String fieldName = ((FieldError) error).getField();
                            String errorMessage = error.getDefaultMessage();
                            errors.put(fieldName, errorMessage);
                        });

        ErrorResponse response =
                ErrorResponse.builder()
                        .statusCode(status.value())
                        .statusMessage(status.getReasonPhrase())
                        .errors(Arrays.asList(errors))
                        .build();

        return ResponseEntity.status(status.value()).body(response);
    }

    private ResponseEntity<ErrorResponse> handleConstraintViolationException(
            HttpStatus status, ConstraintViolationException ex) {
        Map<String, Object> errors = new HashMap<>();

        ex.getConstraintViolations()
                .forEach(
                        (error) -> {
                            String path = error.getPropertyPath().toString();
                            String errorMessage = error.getMessage();
                            errors.put(path, errorMessage);
                        });

        ErrorResponse response =
                ErrorResponse.builder()
                        .statusCode(status.value())
                        .statusMessage(status.getReasonPhrase())
                        .errors(Arrays.asList(errors))
                        .build();

        return ResponseEntity.status(status.value()).body(response);
    }
}
