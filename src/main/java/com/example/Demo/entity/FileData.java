package com.example.Demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.UUID;


@Data
@Entity
@NoArgsConstructor
public class FileData {

    @Id
//    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uuid;

    private String fileName;

    private String fileType;

    private Long size;

    private Timestamp loadTime;

    private Timestamp changeTime;

    private String fileDownloadUri;


}
