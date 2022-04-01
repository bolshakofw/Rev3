package com.example.Demo;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
public class Controller {

    private final FileStorage fileStorage;

    private final FileService fileService;

    public Controller(FileStorage fileStorage, FileService fileService) {
        this.fileStorage = fileStorage;
        this.fileService = fileService;
    }

    @PostMapping
    public void upload(MultipartFile file) throws IOException {
        fileService.upload(file);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID uuid) {
        fileService.delete(uuid);
    }

    @GetMapping
    public List<String> list() {
        return fileService.list();
    }

    @PutMapping("/{uuid}")
    public void change(@PathVariable UUID uuid, @RequestParam String fileName){
        fileService.updateName(uuid,fileName);
    }


    @GetMapping("/download/{uuid}")
    public HttpEntity<byte[]> download(@PathVariable UUID uuid) throws IOException {
        byte[] body = fileStorage.getFileBody(uuid);
        return new HttpEntity<>(
                body,
                createHeader(fileService.getFileName(uuid),body.length)
        );
    }

    private HttpHeaders createHeader(String fileName,int fileSize){
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Disposition","attachment; filename=" + fileName);
        header.setContentLength(fileSize);
        return header;
    }
}
