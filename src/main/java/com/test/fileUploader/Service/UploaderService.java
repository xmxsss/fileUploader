package com.test.fileUploader.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class UploaderService {
    private static String upload_folder="/Users/max/Desktop/files/";
    private static String metadata = "/Users/max/Desktop/metadata";
    public void saveUploadFiles(MultipartFile file) throws IOException {

            byte[] bytes=file.getBytes();
            Path path = Paths.get(upload_folder + file.getOriginalFilename());
            System.out.println(path);
            Files.write(path,bytes);
        }


    public void persistMetaData(MultipartFile file, Integer id)throws IOException{
        Files.write(Paths.get(metadata),
                new String(id+"%%"+file.getOriginalFilename()+"%%"+upload_folder+file.getOriginalFilename()+"\n").getBytes(),StandardOpenOption.APPEND);
    }
}
