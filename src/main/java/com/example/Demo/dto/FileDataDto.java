package com.example.Demo.dto;

import com.example.Demo.entity.FileData;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;


@Data
public class FileDataDto {

    private UUID uuid;
    private String fileName;
    private String fileType;
    private Long size;
    private Timestamp upload;
    private Timestamp change;
    private String link;

    private FileDataDto(FileData fileData) {
        this.uuid = fileData.getUuid();
        this.fileName = fileData.getFileName();
        this.fileType = fileData.getFileType();
        this.size = fileData.getSize();
        this.upload = fileData.getLoadTime();
        this.change = fileData.getChangeTime();
        this.link = "/api/file/download/" + fileData.getUuid().toString();
    }


    public static FileDataDto of(FileData fileData) {
        return new FileDataDto(fileData);
    }


}
