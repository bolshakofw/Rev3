package com.example.Demo;


import com.example.Demo.entity.FileData;
import com.example.Demo.exception.FileDataNotFoundException;
import com.example.Demo.exception.InvalidFileSizeException;
import com.example.Demo.exception.InvalidFileTypeException;
import com.example.Demo.repository.FileRepo;
import com.example.Demo.service.FileService;
import com.example.Demo.service.FileStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class Testing {

    @InjectMocks
    private FileService fileService;

    @Mock
    private FileStorage fileStorage;

    @Mock
    private FileRepo fileRepo;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private MultiFake multiFake;
    /*
    1) все успешно:
        вызов fileRepo.save с ожидаемыми данными
        вызов fileStorage.findFile
    2) ошибки валидации - 2 теста, на каждую ошибку
     */


    @Test
    void repoSave() throws IOException {

        MultiFake fake = new MultiFake("test", "text", MediaType.TEXT_PLAIN_VALUE, new byte[30]);

        FileData fileData1 = new FileData();
        fileData1.setUuid(UUID.randomUUID());
        fileData1.setFileName(fake.getOriginalFilename());
        fileData1.setFileType(fake.getContentType());
        fileData1.setSize(fake.getSize());
        fileData1.setLoadTime(new Timestamp(System.currentTimeMillis()));
        fileData1.setChangeTime(new Timestamp(System.currentTimeMillis()));


        fileService.upload(fake);

        verify(fileRepo).save(argThat(new FileDataMatcher(fileData1)));
    }


    @Test
    void wrongFileType() {
        MockMultipartFile mfile = new MockMultipartFile("test", "text", MediaType.APPLICATION_CBOR_VALUE, new byte[30]);
        assertThatThrownBy(() -> {
            fileService.upload(mfile);
        }).isInstanceOf(InvalidFileTypeException.class);
    }

    @Test
    void wrongFileSize() {
        MockMultipartFile mfile = new MockMultipartFile("test", "test.txt", MediaType.TEXT_PLAIN_VALUE, new byte[999999999]);
        assertThatThrownBy(() -> {
            fileService.upload(mfile);
        }).isInstanceOf(InvalidFileSizeException.class).hasMessageContaining("The file size is more than");
    }


    @Test
    void testDelete() {
        assertThatThrownBy(() -> {
            fileService.delete(Mockito.any());
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    void testGetNames() {
        fileService.list();
        verify(fileRepo).getFilenameList();
    }

    @Test
    void testGetFileName() {
        assertThatThrownBy(() -> {
            fileService.getFileName(Mockito.any());
        }).isInstanceOf(FileDataNotFoundException.class);
    }

}

class FileDataMatcher implements ArgumentMatcher<FileData> {

    private final FileData test;

    public FileDataMatcher(FileData fileData1) {
        this.test = fileData1;
    }


    @Override
    public boolean matches(FileData argument) {

        return test.getFileName().equals(argument.getFileName()) &&
                test.getFileType().equals(argument.getFileType()) &&
                test.getLoadTime().equals(test.getLoadTime()) &&
                test.getChangeTime().equals(test.getChangeTime()) &&
                test.getSize().equals(argument.getSize());
    }
}



