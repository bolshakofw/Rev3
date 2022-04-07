package com.example.Demo.exception.handler;

import com.example.Demo.dto.ExceptionDto;
import com.example.Demo.exception.EmptyFieldException;
import com.example.Demo.exception.FileDataNotFoundException;
import com.example.Demo.exception.InvalidFileSizeException;
import com.example.Demo.exception.InvalidFileTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleInternalErrors(Exception e) {
        log.error("Internal exception", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionDto("Internal exception"));
    }

    @ExceptionHandler(FileDataNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleFileNotFoundException(FileDataNotFoundException e) {
        log.warn("File not found", e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionDto(e.getMessage()));
    }

    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<ExceptionDto> handleWrongFileTypeException(Exception e) {
        log.warn("Invalid file type", e);
        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ExceptionDto(e.getMessage()));
    }

    @ExceptionHandler({InvalidFileSizeException.class, EmptyFieldException.class})
    public ResponseEntity<ExceptionDto> handleWrongFileSizeException(Exception e) {
        log.warn("Incorrect file size: ", e);
        return ResponseEntity
                .badRequest()
                .body(new ExceptionDto(e.getMessage()));
    }


}
