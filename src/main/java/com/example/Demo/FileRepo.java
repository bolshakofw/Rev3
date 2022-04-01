package com.example.Demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileRepo extends JpaRepository<FileData, UUID> {

    @Query("SELECT fileName from  FileData ")
    public List<String> getFilenameList();

}
