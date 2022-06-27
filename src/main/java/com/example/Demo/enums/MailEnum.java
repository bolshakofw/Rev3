package com.example.Demo.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MailEnum {


    FILE_UPLOADED("Your file uploaded successfully", "File uploaded");

    private final String body;

    private final String subject;

}
