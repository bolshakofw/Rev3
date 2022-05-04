package com.example.Demo.utils.model;

import com.example.Demo.entity.FileData;
import org.mockito.ArgumentMatcher;

public class FileDataMatcher implements ArgumentMatcher<FileData> {

    private final FileData test;

    public FileDataMatcher(FileData fileData1) {
        this.test = fileData1;
    }


    @Override
    public boolean matches(FileData argument) {

        return test.getFileName().equals(argument.getFileName()) &&
                test.getFileType().equals(argument.getFileType()) &&
                test.getLoadTime().equals(test.getLoadTime()) &&
                test.getChangeTime().equals(test.getChangeTime()) &&
                test.getSize().equals(argument.getSize());
    }
}