package com.maltsev.clas.service;

import com.maltsev.clas.response.DeletePhotoResponse;
import com.maltsev.clas.response.SavePhotoResponse;
import com.maltsev.jwt.Auth;
import com.maltsev.jwt.UserAccount;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    DeletePhotoResponse deleteImage(UserAccount userAccount);

    SavePhotoResponse saveImage(UserAccount userAccount, MultipartFile file);

    String getImage(UserAccount userAccount);

    String getImageById(String id);

    SavePhotoResponse saveImageForClass(String id, MultipartFile file);
}
