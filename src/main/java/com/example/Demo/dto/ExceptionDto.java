package com.example.Demo.dto;


import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class ExceptionDto {
    private Timestamp timestamp;
    private String message;

    public ExceptionDto(String message) {
        timestamp = new Timestamp(System.currentTimeMillis());
        this.message = message;
    }
}
