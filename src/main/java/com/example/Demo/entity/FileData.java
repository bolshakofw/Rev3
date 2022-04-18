package com.example.Demo.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.UUID;


@Getter
@Setter
@Entity
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

    public FileData(FileData fileData1) {
    }

    public FileData() {

    }
}
