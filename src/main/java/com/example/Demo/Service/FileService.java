package com.example.Demo.Service;


import com.example.Demo.FileData;
import com.example.Demo.exception.EmptyFieldException;
import com.example.Demo.exception.FileDataNotFoundException;
import com.example.Demo.exception.InvalidFileSizeException;
import com.example.Demo.exception.InvalidFileTypeException;
import com.example.Demo.repository.FileRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.xml.transform.Result;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileService {
    private static final List<String> CONTENT_TYPES = List.of("image/png", "image/jpeg", "image/gif", "text/plain", "pdf/application");
    private final long MAX_SIZE = 15 * 1024 * 1024;


    private final FileRepo fileRepo;

    private final FileStorage fileStorage;

    private final EntityManager entityManager;

    public FileService(FileRepo fileRepo, FileStorage fileStorage, EntityManager entityManager) {
        this.fileRepo = fileRepo;
        this.fileStorage = fileStorage;
        this.entityManager = entityManager;
    }


    public void upload(MultipartFile file) throws IOException {
        if (!CONTENT_TYPES.contains((file.getContentType()))) {
            throw new InvalidFileTypeException(file.getContentType() + " not a valid file type , supported file types " + CONTENT_TYPES);
        } else if (!(file.getSize() < MAX_SIZE)) {
            throw new InvalidFileSizeException("The file size is more than " + MAX_SIZE);
        } else if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new EmptyFieldException("Empty filename or file not received");
        }

        FileData fileData = new FileData();
        fileData.setUuid(UUID.randomUUID());
        fileData.setFileName(file.getOriginalFilename());
        fileData.setFileType(file.getContentType());
        fileData.setSize(file.getSize());
        fileData.setLoadTime(new Timestamp(System.currentTimeMillis()));
        fileData.setChangeTime(new Timestamp(System.currentTimeMillis()));

        fileRepo.save(fileData);

        File fileUploadDir = new File(fileStorage.fileUploadDir);
        if (!fileUploadDir.exists())
            fileUploadDir.mkdirs();


        File localFile = fileStorage.findFile(fileData.getUuid());
        file.transferTo(localFile);

    }

    public void delete(UUID uuid) {
        fileStorage.checkExists(uuid);
        fileRepo.deleteById(uuid);
        File localFile = fileStorage.findFile(uuid);
        localFile.delete();
    }

    public List<String> list() {
        return fileRepo.getFilenameList();
    }


    public FileData updateName(UUID uuid, String fileName) {

        fileRepo.getById(uuid);
        FileData fileData = fileStorage.getFileDataById(uuid);
        String type = fileData.getFileName().substring(fileData.getFileName().lastIndexOf(".") + 1);
        fileData.setFileName(fileName + "." + type);
        fileData.setChangeTime(new Timestamp(System.currentTimeMillis()));
        fileRepo.save(fileData);
        return fileData;


    }


    public byte[] getBody(UUID uuid) throws IOException {
        fileStorage.checkExists(uuid);
        return fileStorage.getFileBody(uuid);
    }

    public byte[] downloadZip(UUID[] uuids) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (ZipOutputStream zos = new ZipOutputStream(bos)) {
                for (UUID uuid : uuids) {
                    zos.putNextEntry(new ZipEntry(getFileName(uuid)));
                    zos.write(fileStorage.getFileBody(uuid));
                    zos.closeEntry();
                }
            }
            return bos.toByteArray();
        }
    }


    public String getFileName(UUID uuid) {
        return fileRepo.getFileNameById(uuid)
                .orElseThrow(() -> new FileDataNotFoundException("File with id: " + uuid + " not found"));
    }





}
