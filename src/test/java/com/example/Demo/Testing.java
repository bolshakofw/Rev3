package com.example.Demo;


import com.example.Demo.exception.FileDataNotFoundException;
import com.example.Demo.repository.FileRepo;
import com.example.Demo.service.FileService;
import com.example.Demo.service.FileStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.UnfinishedStubbingException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class Testing {

    @InjectMocks
    private FileService fileService;
    @Mock
    private MultipartFile file;
    @Mock
    private FileStorage fileStorage;
    @Mock
    private FileRepo fileRepo;

    @Test
    void testUpload() {
        Mockito.when(file.getOriginalFilename() != null);
        Assertions.assertThrows(UnfinishedStubbingException.class, () -> fileService.upload(file));
    }

    @Test
    void testDelete() {
        Mockito.doThrow(new FileDataNotFoundException("test")).when(fileStorage).checkExists(Mockito.any());
        Assertions.assertThrows(FileDataNotFoundException.class, () -> fileService.delete(Mockito.any()));
    }

    @Test
    void testGetNames() {
        fileService.list();
        Mockito.verify(fileRepo).getFilenameList();
    }

    @Test
    void testGetFileName() {
        Mockito.when(fileRepo.getFileNameById(Mockito.any())).thenReturn(Optional.empty());
        Assertions.assertThrows(FileDataNotFoundException.class, () -> fileService.getFileName(Mockito.any()));
    }


}
