package com.example.Demo.errors.exception.files;

import java.util.UUID;

public class FileDataNotFoundException extends RuntimeException {
    public FileDataNotFoundException(String message) {
        super(message);
    }

    public static FileDataNotFoundException withUuid(UUID uuid) {
        return new FileDataNotFoundException("File with id: " + uuid + " not found");
    }

}
