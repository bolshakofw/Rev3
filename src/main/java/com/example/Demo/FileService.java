package com.example.Demo;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    private final FileRepo fileRepo;

    private final FileStorage fileStorage;

    public FileService(FileRepo fileRepo, FileStorage fileStorage) {
        this.fileRepo = fileRepo;
        this.fileStorage = fileStorage;
    }


    public void upload(MultipartFile file) throws IOException {
        FileData fileData = new FileData();
        fileData.setUuid(UUID.randomUUID());
        fileData.setFileName(file.getOriginalFilename());
        fileData.setFileType(file.getContentType());
        fileData.setSize(file.getSize());
        fileData.setLoadTime(new Timestamp(System.currentTimeMillis()));
        fileData.setChangeTime(new Timestamp(System.currentTimeMillis()));

        fileRepo.save(fileData);

        File fileUploadDir = new File(fileStorage.fileUploadPath);
        if (!fileUploadDir.exists())
            fileUploadDir.mkdirs();


        File localFile = fileStorage.findLocalFile(fileData.getUuid());
        file.transferTo(localFile);

    }

    public void delete(UUID uuid) {
        fileStorage.checkIfExistsOrElseThrow(uuid);
        fileRepo.deleteById(uuid);
        File localFile = fileStorage.findLocalFile(uuid);
        localFile.delete();
    }

    public List<String> list() {
        return fileRepo.getFilenameList();
    }


    public void updateName(UUID uuid, String fileName) {

        fileRepo.getById(uuid);
        FileData fileData = fileStorage.getFileDataById(uuid);
        String type = fileData.getFileName().substring(fileData.getFileName().lastIndexOf(".")+1);
        fileData.setFileName(fileName+"."+type);
        fileData.setChangeTime(new Timestamp(System.currentTimeMillis()));
        fileRepo.save(fileData);


    }

}
