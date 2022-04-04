package com.example.Demo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;


@Data
@Entity
@Table
public class FileData {
    @Id
    @Column(name = "uuid",updatable = false,nullable = false)
    private UUID uuid;
    @Column(name = "fileName", nullable = false)
    private String fileName;
    @Column(name = "fileType",nullable = false)
    private String fileType;
    @Column(name = "fileSize",nullable = false)
    private Long size;
    @Column(name = "load_time",updatable = false,nullable = false)
    private Timestamp loadTime;
    @Column(name = "change_time",nullable = false)
    private Timestamp changeTime;
    private byte[] bytes;


}
