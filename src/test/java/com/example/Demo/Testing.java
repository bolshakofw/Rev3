package com.example.Demo;


import com.example.Demo.entity.FileData;
import com.example.Demo.errors.exception.files.FileDataNotFoundException;
import com.example.Demo.errors.exception.files.InvalidFileTypeException;
import com.example.Demo.repository.FileRepository;
import com.example.Demo.service.FileService;
import com.example.Demo.service.FileStorage;
import com.example.Demo.utils.model.FileDataMatcher;
import com.example.Demo.utils.model.FileFake;
import com.example.Demo.utils.model.MultiFake;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    private FileRepository fileRepository;


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

        verify(fileRepository).save(argThat(new FileDataMatcher(fileData1)));
    }


    @Test
    @DisplayName("Ошибка неправильного типа")
    void wrongFileType() {
        MockMultipartFile mfile = new MockMultipartFile("test", "text", MediaType.APPLICATION_CBOR_VALUE, new byte[30]);
        assertThatThrownBy(() -> {
            fileService.upload(mfile);
        }).isInstanceOf(InvalidFileTypeException.class);
    }

//    @Test
//    @DisplayName("Ошибка размера файла")
//    void wrongFileSize() {
//        MockMultipartFile mfile = new MockMultipartFile("test", "test.txt", MediaType.TEXT_PLAIN_VALUE, new byte[16 * 1024 * 1024]);
//        assertThatThrownBy(() -> {
//            fileService.upload(mfile);
//        }).isInstanceOf(InvalidFileSizeException.class).hasMessageContaining("The file size is more than");
//    }


    @Test
    @DisplayName("Удаление файла")
        //Переделать
    void testDelete() {
        UUID uuid = UUID.randomUUID();
        Mockito.when(fileStorage.getOrCreateById(uuid)).thenReturn(new FileFake());
        fileService.deleteFile(uuid);
        verify(fileStorage).checkExists(uuid);
        verify(fileRepository).deleteById(uuid);
        verify(fileStorage).getOrCreateById(uuid);

    }

    @Test
    @DisplayName("Список имён файлов")
    void testGetNames() {
        fileService.list();

        verify(fileRepository).getFilenameList();
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
        String fileName = "filename";
        FileData fileData = new FileData();
        fileData.setFileName("Somename.png");
        String fileDataType = ".png";

        Mockito.when(fileStorage.getFileDataById(uuid)).thenReturn(fileData);
        fileService.updateName(uuid, fileName);
        verify(fileRepository).save(fileData);

        assertThat(fileData.getChangeTime()).isNotNull();
        assertThat(fileName + fileDataType).isEqualTo(fileData.getFileName());

    }

}







