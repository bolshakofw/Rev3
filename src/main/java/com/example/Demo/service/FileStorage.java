package com.example.Demo.service;

import com.example.Demo.entity.FileData;
import com.example.Demo.exception.FileDataNotFoundException;
import com.example.Demo.repository.FileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorage {

    private final FileRepo fileRepo;


    @Value("${upload.path}")
    public String fileUploadDir;

    @Autowired
    public FileStorage(FileRepo fileRepo) {
        this.fileRepo = fileRepo;
    }


    public File getOrCreateById(UUID uuid) {
        return new File(fileUploadDir + "/" + uuid);
    }


    public FileData getFileDataById(UUID uuid) {
        return fileRepo.findById(uuid)
                .orElseThrow(() -> new FileDataNotFoundException("File with id: " + uuid + " not found"));
    }


    public void checkExists(UUID uuid) { //uuid, то везде uuid
        if (!fileRepo.existsById(uuid))
            throw new FileDataNotFoundException("File data with id: " + uuid + " not found");
    }

    public byte[] getFileBody(UUID uuid) throws IOException {
        return FileCopyUtils.copyToByteArray(new File(fileUploadDir + "/" + uuid));
    }
}
