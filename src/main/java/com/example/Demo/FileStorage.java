package com.example.Demo;

import com.example.Demo.exception.FileDataNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;

@Service
public class FileStorage {

    private final FileRepo fileRepo;


    @Value("${file.upload-dir}")
    String fileUploadPath;

    public FileStorage(FileRepo fileRepo) {
        this.fileRepo = fileRepo;
    }


    File findLocalFile(UUID uuid) {
        return new File(fileUploadPath + "/" + uuid);
    }

    FileData getFileDataById(UUID uuid){
        return fileRepo.findById(uuid).orElseThrow(()-> new FileDataNotFoundException("File with id: " + uuid + " not found"));
    }


    public void checkIfExistsOrElseThrow(UUID uuid) {
        if (!fileRepo.existsById(uuid))
            throw new FileDataNotFoundException("File data with id: " + uuid + " not found");
    }
}
