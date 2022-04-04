package com.example.Demo.Service;

import com.example.Demo.FileData;
import com.example.Demo.exception.FileDataNotFoundException;
import com.example.Demo.repository.FileRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorage {

    private final FileRepo fileRepo;




    @Value("${file.upload-dir}")
    String fileUploadDir;

    public FileStorage(FileRepo fileRepo) {
        this.fileRepo = fileRepo;
    }


    File findFile(UUID uuid) {
        return new File(fileUploadDir + "/" + uuid);
    }

    FileData getFileDataById(UUID uuid){
        return fileRepo.findById(uuid).orElseThrow(()-> new FileDataNotFoundException("File with id: " + uuid + " not found"));
    }


    public void checkExists(UUID uuid) {
        if (!fileRepo.existsById(uuid))
            throw new FileDataNotFoundException("File data with id: " + uuid + " not found");
    }

    public byte[] getFileBody(UUID uuid) throws IOException {
        return FileCopyUtils.copyToByteArray(new File(fileUploadDir+"/"+uuid));
    }
}
