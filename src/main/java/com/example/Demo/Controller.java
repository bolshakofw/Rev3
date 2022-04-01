package com.example.Demo;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
public class Controller {

    private final FileService fileService;

    public Controller(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public void upload(MultipartFile file) throws IOException {
        fileService.upload(file);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID uuid){
        fileService.delete(uuid);
    }

    @GetMapping
    public List<String> list(){
        return fileService.list();
    }

}
