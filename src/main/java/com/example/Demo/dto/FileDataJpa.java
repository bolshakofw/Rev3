package com.example.Demo.dto;

import com.example.Demo.entity.FileData;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public class FileDataJpa {
    public static final String FILE_NAME = "fileName";
    public static final String FILE_TYPE = "fileType";
    public static final String CHANGE_TIME = "changetime";
    public static volatile SingularAttribute<FileData, String> fileName;
    public static volatile SingularAttribute<FileData, String> fileType;
    public static volatile SingularAttribute<FileData, String> changeTime;

}
