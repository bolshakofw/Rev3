package com.example.Demo.Enums;

public enum MailEnum {


    BODY("Your file uploaded successfully"), SUBJECT("File uploaded");

    private final String mail;

    MailEnum(String mail) {
        this.mail = mail;
    }

    public String get() {
        return mail;
    }


}
