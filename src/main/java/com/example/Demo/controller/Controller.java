package com.example.Demo.controller;


import com.example.Demo.FileData;
import com.example.Demo.Service.FileService;
import com.example.Demo.Service.FileStorage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;


@RequestMapping("/api/file")
@RestController

public class Controller {


    private final FileStorage fileStorage;

    private final FileService fileService;

    public Controller(FileStorage fileStorage, FileService fileService) {
        this.fileStorage = fileStorage;
        this.fileService = fileService;
    }

    //Загрузка файла
    @PostMapping

    public void upload(MultipartFile file) throws IOException {
        fileService.upload(file);
    }

    //Удаление
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID uuid) {
        fileService.delete(uuid);
    }

    //Список имён
    @GetMapping
    public List<String> list() {
        return fileService.list();
    }

    //Изменение имени
    @PutMapping("/{uuid}")
    public FileData change(@PathVariable UUID uuid, @RequestParam String fileName) {
        return fileService.updateName(uuid, fileName);
    }



    //Скачивание одного файла
    @GetMapping("/download/{uuid}")
    public HttpEntity<byte[]> download(@PathVariable UUID uuid) throws IOException {
        byte[] body = fileStorage.getFileBody(uuid);
        return new HttpEntity<>(
                body,
                createHeader(fileService.getFileName(uuid), body.length)
        );
    }


    //Скачивание архива файлов
    @GetMapping("/download/zip")
    public HttpEntity<byte[]> downloadZip(@RequestParam("ids") UUID[] ids) throws IOException {

        byte[] body = fileService.downloadZip(ids);
        return new HttpEntity<>(
                body,
                createHeader("archive.zip", body.length)
        );

    }

    private HttpHeaders createHeader(String fileName, int fileSize) {
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Disposition", "attachment; filename=" + fileName);
        header.setContentLength(fileSize);
        return header;
    }


}
