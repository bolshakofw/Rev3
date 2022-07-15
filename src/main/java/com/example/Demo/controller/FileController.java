package com.example.Demo.controller;


import com.example.Demo.dto.FileDataDto;
import com.example.Demo.dto.SuccessDto;
import com.example.Demo.service.FileService;
import com.example.Demo.service.FileStorage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@RequestMapping("/api/file")
@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileStorage fileStorage;
    private final FileService fileService;

    private String players[];


    private static HttpHeaders createHeader(String fileName, int fileSize) {
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Disposition", "attachment; filename=" + fileName);
        header.setContentLength(fileSize);
        return header;
    }


    @PostMapping
    @Tag(name = "Файлы", description = "Действия с файлами")
    @Operation(summary = "Загрузка файла", description = "Загружает файл в базу данных")
    public ResponseEntity<SuccessDto> upload(MultipartFile file) throws IOException {
        fileService.upload(file);
        return ResponseEntity.ok(new SuccessDto("File uploaded"));
    }


    @DeleteMapping("/{uuid}")
    @Tag(name = "Файлы", description = "Действия с файлами")
    @Operation(summary = "Удаление файла", description = "Удаляет файл по UUID")
    public ResponseEntity<SuccessDto> delete(@PathVariable("uuid") UUID uuid) {
        fileService.deleteFile(uuid);
        return ResponseEntity.ok(new SuccessDto("File deleted successfully"));
    }


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
        return fileService.filter(fileName, fileType, from, till);
    }


    @GetMapping
    @Tag(name = "Имена файлов", description = "Действия с именами файлов")
    @Operation(summary = "Список имён файлов", description = "Выводит список имён всех файлов")
    public List<String> list() {
        return fileService.list();
    }


    @PutMapping("/{uuid}")
    @Tag(name = "Имена файлов", description = "Действия с именами файлов")
    @Operation(summary = "Изменение имени файла", description = "Изменяет имя файла по его UUID")
    public ResponseEntity<SuccessDto> change(@PathVariable UUID uuid, @RequestParam String fileName) {
        fileService.updateName(uuid, fileName);
        return ResponseEntity.ok(new SuccessDto("Filename changed"));
    }


}
