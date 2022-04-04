package com.example.Demo.DTO;

import com.example.Demo.FileData;
import org.apache.tomcat.jni.FileInfo;

import java.util.UUID;

public class FileDataDto {

    private UUID uuid;
    private String fileName;
    private String fileType;
    private long fileSize;
    private long uploadTime;
    private long changeTime;
    private String downloadUri;

    public FileDataDto(UUID uuid, FileData fileData) {
        this.uuid = uuid;
        fileName = fileData.getFileName();
        fileType = fileData.getFileType();
        fileSize = fileData.getSize();
        uploadTime = fileData.getLoadTime().getTime();
        changeTime = fileData.getChangeTime().getTime();
        downloadUri = getUri();
    }

    public FileDataDto(FileData fileData) {
        uuid = fileData.getUuid();
        fileName = fileData.getFileName();
        fileType = fileData.getFileType();
        fileSize = fileData.getSize();
        uploadTime = fileData.getLoadTime().getTime();
        changeTime = fileData.getChangeTime().getTime();
        downloadUri = getUri();
    }

    public FileDataDto(UUID uuid, String name, String type, long size, long uploadTime, long lastChangeTime) {
        this.uuid = uuid;
        this.fileName = name;
        this.fileType = type;
        this.fileSize = size;
        this.uploadTime = uploadTime;
        this.changeTime = lastChangeTime;
        downloadUri = getUri();
    }

    public FileDataDto(FileInfo fileInfo) {
    }

    private String getUri() {
        return "/api/file" + uuid.toString();
    }
}
