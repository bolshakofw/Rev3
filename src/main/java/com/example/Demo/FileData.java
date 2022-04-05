package com.example.Demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

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

    private UUID uuid;

    private String fileName;

    private String fileType;

    private Long size;

    private Timestamp loadTime;

    private Timestamp changeTime;
//    @JsonIgnore
//    private byte[] bytes;


}
