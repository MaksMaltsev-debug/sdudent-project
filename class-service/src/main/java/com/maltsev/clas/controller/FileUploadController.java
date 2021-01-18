package com.maltsev.clas.controller;

import com.maltsev.clas.response.DeletePhotoResponse;
import com.maltsev.clas.response.SavePhotoResponse;
import com.maltsev.clas.service.ImageService;
import com.maltsev.jwt.Auth;
import com.maltsev.jwt.UserAccount;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

import static com.maltsev.clas.constants.URIConstants.CLASS_IMAGE;
import static com.maltsev.clas.constants.URIConstants.IMAGE;
import static org.apache.tomcat.util.http.fileupload.FileUploadBase.MULTIPART_FORM_DATA;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@AllArgsConstructor
@RestController
@CrossOrigin("*")
public class FileUploadController {
    private final ImageService imageService;

    @PostMapping(value = IMAGE, consumes = MULTIPART_FORM_DATA)
    public SavePhotoResponse saveImage(@Auth UserAccount userAccount,
                                       @RequestParam("file") MultipartFile file) {
        return imageService.saveImage(userAccount, file);
    }
    @DeleteMapping(value = IMAGE)
    public DeletePhotoResponse deleteImage(@Auth UserAccount userAccount) {
        return imageService.deleteImage(userAccount);
    }

    @PostMapping(value = CLASS_IMAGE, consumes = MULTIPART_FORM_DATA)
    public SavePhotoResponse saveImage(@RequestParam String id,
                                       @RequestParam("file") MultipartFile file) {
        return imageService.saveImageForClass(id, file);
    }

    @GetMapping(value = IMAGE, produces = IMAGE_JPEG_VALUE)
    public String getImage(@Auth UserAccount userAccount) {
        return imageService.getImage(userAccount);
    }
}
