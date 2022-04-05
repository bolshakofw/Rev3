package com.example.Demo.Service;


import com.example.Demo.DTO.FileDataDto;
import com.example.Demo.FileData;
import com.example.Demo.exception.EmptyFieldException;
import com.example.Demo.exception.FileDataNotFoundException;
import com.example.Demo.exception.InvalidFileSizeException;
import com.example.Demo.exception.InvalidFileTypeException;
import com.example.Demo.repository.FileRepo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
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

    public List<FileDataDto> filter(String name, String type, Long from, Long till) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FileData> query = builder.createQuery(FileData.class);
        Root<FileData> root = query.from(FileData.class);
        List<Predicate> list = new LinkedList<>();

        if (StringUtils.hasLength(name))
            list.add(builder.like(root.get("fileName"), "%" + name + "%"));
        if (StringUtils.hasLength(type))
            list.add(builder.like(root.get("fileType"), "%" + type + "%"));
        if (!Objects.isNull(from))
            list.add(builder.greaterThan(root.get("change_time"), new Timestamp(from)));
        if (!Objects.isNull(till))
            list.add(builder.lessThan(root.get("change_time"), new Timestamp(till)));

        query.where(list.toArray(new Predicate[0]));

        TypedQuery<FileData> fileInfoTypedQuery = entityManager.createQuery(query);
        return fileInfoTypedQuery.getResultList().stream().map(FileDataDto::new).collect(Collectors.toList());

    }

    public byte[] getBody(UUID id) throws IOException {
        fileStorage.checkExists(id);
        return fileStorage.getFileBody(id);
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
