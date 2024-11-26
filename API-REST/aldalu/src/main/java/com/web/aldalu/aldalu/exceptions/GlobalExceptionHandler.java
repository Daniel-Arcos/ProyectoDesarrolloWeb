package com.web.aldalu.aldalu.exceptions;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.web.aldalu.aldalu.exceptions.dtos.AppException;
import com.web.aldalu.aldalu.exceptions.dtos.ExceptionDTO;
import com.web.aldalu.aldalu.utils.ExceptionMessageConstants;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<ExceptionDTO> buildResponseEntity(HttpStatus status, String message, WebRequest request) {
        ExceptionDTO error = new ExceptionDTO();
        error.setStatus(status.value());
        error.setError(status.getReasonPhrase());
        error.setMessage(message);
        error.setPath(request.getDescription(false).substring(4)); // Remover "uri="
        return new ResponseEntity<>(error, status);
    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionDTO> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return buildResponseEntity(status, ex.getMessage(), request);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ExceptionDTO> handleAppException(AppException ex, WebRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getResponseCode());
        return buildResponseEntity(status, ex.getMessage(), request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleNotFoundException(NotFoundException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    // @ExceptionHandler(RequestException.class)
    // public ResponseEntity<ExceptionDTO> handleRequestException(RequestException ex, WebRequest request) {
    //     LOGGER.warn("RequestException: {}", ex.getMessage(), ex);
    //     return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    // }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ExceptionDTO> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionDTO> handleIllegalStateExceptionn(IllegalStateException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }


    // @ExceptionHandler(Duplicate.class)
    // public ResponseEntity<ExceptionDTO> handleDuplicateKeyException(DuplicateKeyException ex, WebRequest request) {
    //     LOGGER.warn("DuplicateKeyException: {}", ex.getMessage(), ex);
    //     return buildResponseEntity(HttpStatus.CONFLICT, ex.getMessage(), request);
    // }

    // @ExceptionHandler(InternalServerErrorException.class)
    // public ResponseEntity<ExceptionDTO> handleInternalServerErrorException(InternalServerErrorException ex, WebRequest request) {
    //     LOGGER.error("InternalServerErrorException: {}", ex.getMessage(), ex);
    //     return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionMessageConstants.INTERNAL_SERVER_ERROR_MSG, request);
    // }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionDTO> handleNullPointerException(NullPointerException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionMessageConstants.INTERNAL_SERVER_ERROR_MSG, request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDTO> handleRuntimeException(RuntimeException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionMessageConstants.INTERNAL_SERVER_ERROR_MSG, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleGenericException(Exception ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionMessageConstants.INTERNAL_SERVER_ERROR_MSG, request);
    }
}
