package com.example.Demo.repository;

import com.example.Demo.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileRepo extends JpaRepository<FileData, UUID> {

    @Query("SELECT fileName from  FileData ")
    public List<String> getFilenameList();

    @Query("SELECT fileName from FileData where uuid = :uuid")
    Optional<String> getFileNameById(UUID uuid);



}
