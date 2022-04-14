package com.example.Demo;


import com.example.Demo.exception.FileDataNotFoundException;
import com.example.Demo.exception.InvalidFileSizeException;
import com.example.Demo.exception.InvalidFileTypeException;
import com.example.Demo.repository.FileRepo;
import com.example.Demo.service.FileService;
import com.example.Demo.service.FileStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class Testing {

    @InjectMocks
    private FileService fileService;

    @Mock
    private FileStorage fileStorage;

    @Mock
    private FileRepo fileRepo;

    /*
    1) все успешно:
        вызов fileRepo.save с ожидаемыми данными
        вызов fileStorage.findFile
    2) ошибки валидации - 2 теста, на каждую ошибку
     */

//    @Test
//    @DisplayName("Успешная загрузка файла")
//    void whenCorrectFile_thenSuccess() {
//        new MockMultipartFile("test.text", (String) null, (String) null, (byte[]) null);
//        Mockito.when().thenReturn();
//    }


    @Test
    void repoSave() throws IOException {

        MockMultipartFile file = new MockMultipartFile("test", "text", MediaType.TEXT_PLAIN_VALUE, new byte[30]);
        FileData fileData1 = new FileData();
        fileData1.setUuid(UUID.randomUUID());
        fileData1.setFileName(file.getOriginalFilename());
        fileData1.setFileType(file.getContentType());
        fileData1.setSize(file.getSize());
        fileData1.setLoadTime(new Timestamp(System.currentTimeMillis()));
        fileData1.setChangeTime(new Timestamp(System.currentTimeMillis()));
        fileData1.setFileDownloadUri("/api/file/download/" + fileData1.getUuid().toString());

        Mockito.when(fileStorage.findFile(Mockito.any())).thenReturn(new File("C:/Users/karimullin-ai/uploads" + "/" + fileData1.getUuid().toString()));


        fileService.upload(file);
        Mockito.verify(fileRepo).save((fileData1));
    }


    @Test
    void wrongFileType() {
        MockMultipartFile mfile = new MockMultipartFile("test", "text", MediaType.APPLICATION_CBOR_VALUE, new byte[30]);
        Assertions.assertThrows(InvalidFileTypeException.class, () -> fileService.upload(mfile));
    }

    @Test
    void wrongFileSize() {
        MockMultipartFile mfile = new MockMultipartFile("test", "test.txt", MediaType.TEXT_PLAIN_VALUE, new byte[999999999]);
        Assertions.assertThrows(InvalidFileSizeException.class, () -> fileService.upload(mfile));
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

    @Test
    void testCheckExists() {
        Mockito.doThrow(new FileDataNotFoundException("test")).when(fileStorage).checkExists(Mockito.any());
        Assertions.assertThrows(FileDataNotFoundException.class, () -> fileService.getFile(Mockito.any()));
    }


}