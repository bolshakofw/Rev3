package com.example.Demo.DTO;

import com.example.Demo.FileData;
import lombok.Getter;

import java.util.UUID;


@Getter
public class FileDataDto {

    private UUID uuid;
    private String name;
    private String type;
    private long size;
    private long uploadTime;
    private long lastChangeTime;
    private String downloadUri;

    public FileDataDto(UUID uuid, String name, String type, long size, long uploadTime, long lastChangeTime) {
        this.uuid = uuid;
        this.name = name;
        this.type = type;
        this.size = size;
        this.uploadTime = uploadTime;
        this.lastChangeTime = lastChangeTime;
        downloadUri = getUri();
    }

    public FileDataDto(FileData fileData) {
        uuid = fileData.getUuid();
        name = fileData.getFileName();
        type = fileData.getFileType();
        size = fileData.getSize();
        uploadTime = fileData.getLoadTime().getTime();
        lastChangeTime = fileData.getChangeTime().getTime();
        downloadUri = getUri();
    }


    private String getUri(){
        return "/api/file" + uuid.toString();
    }
}
