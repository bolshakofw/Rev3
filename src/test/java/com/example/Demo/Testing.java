package com.example.Demo;


import com.example.Demo.entity.FileData;
import com.example.Demo.exception.FileDataNotFoundException;
import com.example.Demo.exception.InvalidFileSizeException;
import com.example.Demo.exception.InvalidFileTypeException;
import com.example.Demo.repository.FileRepo;
import com.example.Demo.service.FileService;
import com.example.Demo.service.FileStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

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


    @Test
    @DisplayName("Успешная загрузка")
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
    @DisplayName("Ошибка неправильного типа")
    void wrongFileType() {
        MockMultipartFile mfile = new MockMultipartFile("test", "text", MediaType.APPLICATION_CBOR_VALUE, new byte[30]);
        assertThatThrownBy(() -> {
            fileService.upload(mfile);
        }).isInstanceOf(InvalidFileTypeException.class);
    }

    @Test
    @DisplayName("Ошибка размера файла")
    void wrongFileSize() {
        MockMultipartFile mfile = new MockMultipartFile("test", "test.txt", MediaType.TEXT_PLAIN_VALUE, new byte[16 * 1024 * 1024]);
        assertThatThrownBy(() -> {
            fileService.upload(mfile);
        }).isInstanceOf(InvalidFileSizeException.class).hasMessageContaining("The file size is more than");
    }


    @Test
    @DisplayName("Удаление файла")
        //Переделатть
    void testDelete() {
        UUID uuid = UUID.randomUUID();
        assertThatThrownBy(() -> {
            fileService.delete(uuid);
        }).isInstanceOf(FileDataNotFoundException.class);
    }

    @Test
    @DisplayName("Список имён файлов")
    void testGetNames() {
        fileService.list();
        verify(fileRepo).getFilenameList();
    }

    @Test
    @DisplayName("Ошибка получения имени файла по его id")
    void testGetFileName() {
        UUID id = UUID.randomUUID();
        assertThatThrownBy(() -> {
            fileService.getFileName(id);
        }).isInstanceOf(FileDataNotFoundException.class).hasMessage("File with id: " + id + " not found");
    }

    @Test
    void updateNameTest() {
        UUID uuid = UUID.randomUUID();
        String fileName = "filename.png";
        FileData fileData = new FileData();
        fileData.setFileName("Somename.hshs");
        Mockito.when(fileStorage.getFileDataById(uuid)).thenReturn(fileData);
        fileService.updateName(uuid, fileName);
        Mockito.verify(fileRepo).save(fileData);
    }

}

//update

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



