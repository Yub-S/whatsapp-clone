package com.yubraj.whatsapp_clone.message;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    public static byte[] readFileFromLocation(String fileUrl){
        if (fileUrl == null ||fileUrl.isBlank()){
            return new byte[0];
        }
        try {
            Path file = Paths.get(fileUrl);
            return Files.readAllBytes(file);
        }catch (IOException e){
            System.out.println("unable to read the file");
        }
        return new byte[0];
    }
}
