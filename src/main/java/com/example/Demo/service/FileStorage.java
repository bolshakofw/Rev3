package com.example.Demo.service;

import com.example.Demo.entity.FileData;
import com.example.Demo.errors.exception.FileDataNotFoundException;
import com.example.Demo.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorage {

    private final FileRepository fileRepo;


    @Value("${upload.path}")
    private String fileUploadDir;


    public File getOrCreateById(UUID uuid) {
        return new File(fileUploadDir + "/" + uuid);
    }


    public FileData getFileDataById(UUID uuid) {
        return fileRepo.findById(uuid)
                .orElseThrow(() -> FileDataNotFoundException.withUuid(uuid));
    }


    public void checkExists(UUID uuid) {
        if (!fileRepo.existsById(uuid))
            throw FileDataNotFoundException.withUuid(uuid);
    }

    public byte[] getFileBody(UUID uuid) throws IOException {
        return FileCopyUtils.copyToByteArray(new File(fileUploadDir + "/" + uuid));
    }
}
