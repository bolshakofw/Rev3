package com.example.Demo.service;


import com.example.Demo.FileData;
import com.example.Demo.exception.EmptyFieldException;
import com.example.Demo.exception.FileDataNotFoundException;
import com.example.Demo.exception.InvalidFileSizeException;
import com.example.Demo.exception.InvalidFileTypeException;
import com.example.Demo.repository.FileRepo;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileService {
    private static final List<String> CONTENT_TYPES = List.of("image/png", "image/jpeg", "image/gif", "text/plain", "pdf/application");
    private final long MAX_SIZE = 15 * 1024 * 1024;


    private final FileRepo fileRepo;

    private final FileStorage fileStorage;


    public FileService(FileRepo fileRepo, FileStorage fileStorage) {
        this.fileRepo = fileRepo;
        this.fileStorage = fileStorage;

    }


    public FileData upload(MultipartFile file) throws IOException {
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
        fileData.setFileDownloadUri("/api/file/download/" + fileData.getUuid().toString());

        fileRepo.save(fileData);

        file.transferTo(fileStorage.findFile(fileData.getUuid()));
        return fileData;
    }

    public void delete(UUID id) {
        fileStorage.checkExists(id);
        fileRepo.deleteById(id);
        File localFile = fileStorage.findFile(id);
        localFile.delete();
    }

    public List<String> list() {
        return fileRepo.getFilenameList();
    }


    public void updateName(UUID id, String fileName) {

        fileRepo.getById(id);
        FileData fileData = fileStorage.getFileDataById(id);
        String type = fileData.getFileName().substring(fileData.getFileName().lastIndexOf(".") + 1);
        fileData.setFileName(fileName + "." + type);
        fileData.setChangeTime(new Timestamp(System.currentTimeMillis()));
        fileRepo.save(fileData);

    }

    public List<FileData> filter(String name, String type, Long from, Long till) {
        return fileRepo.findAll(getFileDataSpecification(name, type, from, till));
    }

    private Specification<FileData> getFileDataSpecification(String name, String type, Long from, Long till) {
        return (root, query, builder) -> {
            List<Predicate> list = new LinkedList<>();
            if (StringUtils.hasLength(name))
                list.add(builder.like(root.get("fileName"), "%" + name + "%"));
            if (StringUtils.hasLength(type))
                list.add(builder.like(root.get("fileType"), "%" + type + "%"));
            if (!Objects.isNull(from))
                list.add(builder.greaterThan(root.get("changeTime"), new Timestamp(from)));
            if (!Objects.isNull(till))
                list.add(builder.lessThan(root.get("changeTime"), new Timestamp(till)));
            return builder.or(list.toArray(Predicate[]::new));
        };

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


    public byte[] getFile(UUID uuid) throws IOException {
        fileStorage.checkExists(uuid);
        return fileStorage.getFileBody(uuid);
    }

}
