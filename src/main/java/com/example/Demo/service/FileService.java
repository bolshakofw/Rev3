package com.example.Demo.service;

import com.example.Demo.config.SpringSecurityConfig;
import com.example.Demo.controller.AuthController;
import com.example.Demo.dto.FileDataDto;
import com.example.Demo.entity.FileData;
import com.example.Demo.entity.FileData_;
import com.example.Demo.entity.UserProfile;
import com.example.Demo.entity.UserProfile_;
import com.example.Demo.errors.exception.EmptyFieldException;
import com.example.Demo.errors.exception.FileDataNotFoundException;
import com.example.Demo.errors.exception.InvalidFileTypeException;
import com.example.Demo.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class FileService {
    private static final List<String> CONTENT_TYPES = List.of("image/png", "image/jpeg", "image/gif", "text/plain", "pdf/application");

    private final FileRepository fileRepo;
    private final FileStorage fileStorage;
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthService authService;

    public FileData upload(MultipartFile file) throws IOException {
        if (!CONTENT_TYPES.contains((file.getContentType()))) {
            throw new InvalidFileTypeException(file.getContentType() + " not a valid file type , supported file types " + CONTENT_TYPES);
        } else if (!StringUtils.hasLength(file.getOriginalFilename())) {
            throw new EmptyFieldException("Empty filename or file not received");
        }






        FileData fileData = new FileData();
        fileData.setFileName(file.getOriginalFilename());
        fileData.setFileType(file.getContentType());
        fileData.setSize(file.getSize());
        Timestamp loadTime = new Timestamp(System.currentTimeMillis());
        fileData.setLoadTime(loadTime);
        fileData.setChangeTime(loadTime);


        fileRepo.save(fileData);
        file.transferTo(fileStorage.getOrCreateById(fileData.getUuid()));
        return fileData;
    }

    public void deleteFile(UUID uuid) {
        fileStorage.checkExists(uuid);
        fileRepo.deleteById(uuid);
        File file = fileStorage.getOrCreateById(uuid);
        file.delete();
    }

    public List<String> list() {
        return fileRepo.getFilenameList();
    }


    public void updateName(UUID id, String fileName) {
        fileStorage.checkExists(id);
        FileData fileData = fileStorage.getFileDataById(id);
        String oldName = fileData.getFileName();
        String type = oldName.substring(oldName.lastIndexOf("."));
        fileData.setFileName(fileName + type);
        fileData.setChangeTime(new Timestamp(System.currentTimeMillis()));
        fileRepo.save(fileData);
    }

    public List<FileDataDto> filter(String name, String type, Long from, Long till) {
        return fileRepo.findAll(getFileDataSpecification(name, type, from, till)).stream()
                .map(FileDataDto::of)
                .collect(Collectors.toList());
    }

    private Specification<FileData> getFileDataSpecification(String name, String type, Long from, Long till) {
        return (root, query, builder) -> {
            List<Predicate> list = new LinkedList<>();
            if (StringUtils.hasLength(name))
                list.add(builder.like(builder.lower(root.get(FileData_.FILE_NAME)), "%" + name.toLowerCase() + "%"));
            if (StringUtils.hasLength(type))
                list.add(builder.like(builder.lower(root.get(FileData_.FILE_TYPE)), "%" + type.toLowerCase() + "%"));
            if (!Objects.isNull(from))
                list.add(builder.greaterThan(root.get(FileData_.CHANGE_TIME), new Timestamp(from)));
            if (!Objects.isNull(till))
                list.add(builder.lessThan(root.get(FileData_.CHANGE_TIME), new Timestamp(till)));
            return builder.and(list.toArray(Predicate[]::new));
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
                .orElseThrow(() -> FileDataNotFoundException.withUuid(uuid));
    }
}
