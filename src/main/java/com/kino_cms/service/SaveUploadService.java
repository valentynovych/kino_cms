package com.kino_cms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SaveUploadService {
    @Value("${upload.path}")
    private String uploadPath;

    public List<String> saveUploadFiles(List<MultipartFile> multipartFileList, List<String> imageNames) throws IOException {

        ArrayList<String> fileNameList = new ArrayList<>(imageNames);
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        for (int i = 0; i < fileNameList.size(); i++) {
            MultipartFile image = multipartFileList.get(i);
            if (image.getOriginalFilename().equals("empty.png")) {
                File deleteImage = new File(uploadPath + "/" + fileNameList.get(i));
                deleteImage.delete();
                fileNameList.set(i, null);
            } else if (!image.isEmpty()) {
                String uuidFile = UUID.randomUUID().toString();
                String fileName = uuidFile + "_" + image.getOriginalFilename();
                image.transferTo(new File(uploadPath + "/" + fileName));
                fileNameList.set(i, fileName);
            }
        }
        return fileNameList;
    }
}