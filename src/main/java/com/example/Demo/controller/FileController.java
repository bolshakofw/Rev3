package com.example.Demo.controller;


import com.example.Demo.dto.FileDataDto;
import com.example.Demo.entity.FileData;
import com.example.Demo.service.FileService;
import com.example.Demo.service.FileStorage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@RequestMapping("/api/file")
@RestController
// todo название *
@RequiredArgsConstructor
public class FileController {


    private final FileStorage fileStorage;

    private final FileService fileService;

    // todo Lombok *


    private static HttpHeaders createHeader(String fileName, int fileSize) {
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Disposition", "attachment; filename=" + fileName);
        header.setContentLength(fileSize);
        return header;
    }

    //Загрузка файла
    @PostMapping
    @Tag(name = "Файлы", description = "Действия с файлами")
    @Operation(summary = "Загрузка файла", description = "Загружает файл в базу данных")
    public FileData upload(MultipartFile file) throws IOException {
        return fileService.upload(file);
    }

    //Удаление
    @DeleteMapping("/{uuid}")
    @Tag(name = "Файлы", description = "Действия с файлами")
    @Operation(summary = "Удаление файла", description = "Удаляет файл по UUID")
    public void delete(@PathVariable("uuid") UUID uuid) {
        fileService.delete(uuid);
    }

    //Скачивание одного файла
    @GetMapping("/download/{uuid}")
    @Tag(name = "Файлы", description = "Действия с файлами")
    @Operation(summary = "Скачивание файла", description = "Скачивает файл по UUID")
    public HttpEntity<byte[]> download(@PathVariable UUID uuid) throws IOException {
        byte[] body = fileStorage.getFileBody(uuid);
        return new HttpEntity<>(
                body,

                createHeader(fileService.getFileName(uuid), body.length)
        );
    }

    //Скачивание архива файлов
    @GetMapping("/download/zip")
    @Tag(name = "Файлы", description = "Действия с файлами")
    @Operation(summary = "Скачивание архива файлов", description = "Скачивает архив файлов по UUID")
    public HttpEntity<byte[]> downloadZip(@RequestParam("ids") UUID[] ids) throws IOException {
        byte[] body = fileService.downloadZip(ids);
        return new HttpEntity<>(
                body,
                createHeader("archive.zip", body.length)
        );

    }

    @GetMapping("/filter")
    @Tag(name = "Имена файлов", description = "Действия с именами файлов")
    @Operation(summary = "Список файлов", description = "Выводит список моделей всех файлов")
    public List<FileDataDto> filterr(@RequestParam(required = false, name = "fileName") String fileName,
                                     @RequestParam(required = false, name = "fileType") String fileType,
                                     @RequestParam(required = false, name = "from") Long from,
                                     @RequestParam(required = false, name = "till") Long till) {
        return fileService.filter(fileName.toLowerCase(), fileType.toLowerCase(), from, till);
    }

    //Список имён
    @GetMapping
    @Tag(name = "Имена файлов", description = "Действия с именами файлов")
    @Operation(summary = "Список имён файлов", description = "Выводит список имён всех файлов")
    public List<String> list() {
        return fileService.list();
    }


    //Изменение имени
    @PutMapping("/{uuid}")
    @Tag(name = "Имена файлов", description = "Действия с именами файлов")
    @Operation(summary = "Изменение имени файла", description = "Изменяет имя файла по его UUID")
    public void change(@PathVariable UUID uuid, @RequestParam String fileName) {
        fileService.updateName(uuid, fileName);
    }


}