package com.test.fileUploader.Service;

import com.test.fileUploader.Po.FileMetaData;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {


    public List<FileMetaData> findAllFiles() throws IOException {
       return Files.lines(Paths.get("/Users/min/Desktop/metadata")).map(line->{
           FileMetaData fileMetaData=new FileMetaData();
           fileMetaData.setId(Integer.parseInt(line.split("%%")[0]));
           fileMetaData.setFileName(line.split("%%")[1]);
           fileMetaData.setPath(line.split("%%")[2]);
           return fileMetaData;
       }).collect(Collectors.toList());
    }
    public FileMetaData findFilesByFileName(String fileName)throws IOException{
        for(FileMetaData file: findAllFiles()){
            if(file.getFileName().indexOf(fileName)>=0) return file;
        }
        return null;
    }
}
