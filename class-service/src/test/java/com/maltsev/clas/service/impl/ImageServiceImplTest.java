package com.maltsev.clas.service.impl;

import com.maltsev.clas.entity.Image;
import com.maltsev.clas.exception.SavePhotoException;
import com.maltsev.clas.repository.ImageRepository;
import com.maltsev.clas.service.ImageService;
import com.maltsev.jwt.UserAccount;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.maltsev.clas.service.impl.constants.TestConstants.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageServiceImplTest {
    @MockBean
    private ImageRepository imageRepository;
    @Mock
    private MultipartFile file;
    private ImageService imageService;

    @Before
    public void setUp() {
        imageService = new ImageServiceImpl(imageRepository);
    }

    @Test
    public void saveImage_WhenRequestIsValid_ExpectMethodSave() throws IOException {
        UserAccount account = UserAccount.builder()
                .userId(DEFAULT_ID)
                .build();
        byte[] img = new byte[NINE];
        when(file.isEmpty()).thenReturn(false);
        when(file.getBytes()).thenReturn(img);
        when(file.getSize()).thenReturn((long) NINE);
        imageService.saveImage(account, file);
        verify(imageRepository, times(1)).save(any());
    }
    @Test(expected = SavePhotoException.class)
    public void saveImage_WhenFileIsEmpty_ExpectSavePhotoException() throws IOException {
        UserAccount account = UserAccount.builder()
                .userId(DEFAULT_ID)
                .build();
        when(file.isEmpty()).thenReturn(true);
        when(file.getSize()).thenReturn((long) NINE);
        imageService.saveImage(account, file);
    }
    @Test(expected = SavePhotoException.class)
    public void saveImage_WhenFileHasBigSize_ExpectSavePhotoException() throws IOException {
        UserAccount account = UserAccount.builder()
                .userId(DEFAULT_ID)
                .build();
        when(file.isEmpty()).thenReturn(false);
        when(file.getSize()).thenReturn((long) 900000);
        imageService.saveImage(account, file);
    }
    @Test(expected = SavePhotoException.class)
    public void saveImage_WhenIOException_ExpectSavePhotoException() throws IOException {
        UserAccount account = UserAccount.builder()
                .userId(DEFAULT_ID)
                .build();
        when(file.isEmpty()).thenReturn(false);
        when(file.getSize()).thenReturn((long) NINE);
        when(file.getBytes()).thenThrow(IOException.class);
        imageService.saveImage(account, file);
    }
//    @Test
//    public void getImage_WhenRequestIsValid_ExpectBytesMas() {
//        UserAccount account = UserAccount.builder()
//                .userId(DEFAULT_ID)
//                .build();
//        byte[] imgMas = new byte[NINE];
//        Image image = new Image(DEFAULT_ID, imgMas);
//        when(imageRepository.findByUserId(any())).thenReturn(java.util.Optional.of(image));
//        byte[] response = imageService.getImage(account);
//        assertEquals(imgMas, response);
//    }
}