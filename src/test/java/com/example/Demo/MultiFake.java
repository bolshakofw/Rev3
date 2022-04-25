package com.example.Demo;

import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;

public class MultiFake extends MockMultipartFile {

    public MultiFake(String name, String originalFilename, String contentType, byte[] content) {
        super(name, originalFilename, contentType, content);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {

    }


}
