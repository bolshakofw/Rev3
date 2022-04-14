package com.example.Demo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;


@Getter
@Setter
@Entity
@Table
@NoArgsConstructor
public class FileData {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uuid;


    private String fileName;

    private String fileType;

    private Long size;

    private Timestamp loadTime;

    private Timestamp changeTime;


    private String fileDownloadUri;


    public FileData(UUID uuid, String fileName, String fileType, Long size, Timestamp loadTime, Timestamp changeTime) {
        this.uuid = uuid;
        this.fileName = fileName;
        this.fileType = fileType;
        this.size = size;
        this.loadTime = loadTime;
        this.changeTime = changeTime;
    }


    public FileData(MultipartFile file) {
        this.fileName = file.getOriginalFilename();
        this.fileType = file.getContentType();
        this.size = file.getSize();
        this.loadTime = new Timestamp(System.currentTimeMillis());
        this.changeTime = new Timestamp(System.currentTimeMillis());
    }
}
