package com.maltsev.clas.controller;

import com.maltsev.clas.controller.advice.WebExceptionHandler;
import com.maltsev.clas.response.SavePhotoResponse;
import com.maltsev.clas.service.ImageService;
import com.maltsev.jwt.UserAccount;
import com.maltsev.jwt.resolver.TestAuthenticationArgumentResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import static com.maltsev.clas.constants.URIConstants.IMAGE;
import static com.maltsev.clas.service.impl.constants.TestConstants.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileUploadControllerTest {

    @MockBean
    private ImageService imageService;
    @Mock
    private UserAccount account;

    private MockMvc mvc;

    @Mock
    private MultipartFile file;

    @Before
    public void setup() {
        TestAuthenticationArgumentResolver resolver = new TestAuthenticationArgumentResolver(account);
        mvc = MockMvcBuilders.standaloneSetup(new FileUploadController(imageService))
                .setControllerAdvice(new WebExceptionHandler())
                .setCustomArgumentResolvers(resolver)
                .build();
    }

    @Test
    public void saveImage() throws Exception {
        byte[] content = {};
        UserAccount account = UserAccount.builder()
                .userId(DEFAULT_ID)
                .accessToken(DEFAULT_ACCESS_TOKEN)
                .build();
        SavePhotoResponse response = new SavePhotoResponse(RESPONSE_IMAGE);
        when(imageService.saveImage(any(), any())).thenReturn(response);
        SavePhotoResponse result = imageService.saveImage(account, file);
        mvc.perform(MockMvcRequestBuilders.multipart(IMAGE)
                .file("file", content)
                .header(AUTHORIZATION, DEFAULT_ACCESS_TOKEN))
                .andExpect(status().isOk());
    }

//    @Test
//    public void getImage() throws Exception {
//        byte[] image = {};
//        UserAccount account = UserAccount.builder()
//                .userId(DEFAULT_ID)
//                .accessToken(DEFAULT_ACCESS_TOKEN)
//                .build();
//        when(imageService.getImage(any())).thenReturn(image);
//        byte[] response = imageService.getImage(account);
//        mvc.perform(MockMvcRequestBuilders.get(IMAGE)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//        assertEquals(image, response);
//    }
}