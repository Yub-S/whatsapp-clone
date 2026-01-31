package com.yubraj.whatsapp_clone.message;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.System.currentTimeMillis;

@Service
@Slf4j
public class FileService {

    @Value("${application.file.uploads.media-output.path}")
    private String fileUploadPath;

    public String saveFile( MultipartFile sourceFile, String userId){

        final String fileUploadSubPath = "users" + File.separator + userId;
        return uploadFile(sourceFile,fileUploadSubPath);
    }

    private String uploadFile(MultipartFile sourceFile, String fileUploadSubPath) {
        final String finalUploadPath = fileUploadPath + File.separator + fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);
        if (!targetFolder.exists()){
            boolean folderCreated = targetFolder.mkdirs();
            if(!folderCreated){
                log.warn("failed to create the target folder: {}",targetFolder.getAbsolutePath());
            }
        }
        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        String fileName = currentTimeMillis()+ "." +fileExtension;
        final String targetFilePath = finalUploadPath + File.separator + fileName;
        Path targetPath = Paths.get(targetFilePath);
        try{
            Files.write(targetPath,sourceFile.getBytes());
            log.info("file saved to : "+targetFilePath);
            return fileUploadSubPath + File.separator + fileName;
        }catch (IOException e){

            log.error("File was not saved", e);
        }
        return null;
    }

    private String getFileExtension(@Nullable String originalFilename) {
        if (originalFilename == null || originalFilename.isEmpty()){
            return "";
        }
        int lastDotIndex = originalFilename.lastIndexOf('.');
        if (lastDotIndex==-1){
            return "";
        }
        return originalFilename.substring(lastDotIndex+1).toLowerCase();
    }
}
