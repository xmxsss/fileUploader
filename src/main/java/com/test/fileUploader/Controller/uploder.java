package com.test.fileUploader.Controller;

import com.test.fileUploader.Po.FileMetaData;
import com.test.fileUploader.Po.FilesMetadata;
import com.test.fileUploader.Service.FileService;
import com.test.fileUploader.Service.UploaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class uploder {
public List<String> getId() throws IOException{
    return Files.lines(Paths.get("/Users/min/Desktop/metadata")).map(line->line.split("%%")[0]).collect(Collectors.toList());
}

    AtomicInteger id;
    @Autowired
    UploaderService u;
    @Autowired
    FileService f;
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file)throws IOException{
        if(file.isEmpty()) return new ResponseEntity("please choose a file", HttpStatus.OK);
        System.out.println("got your file! "+file.getOriginalFilename());

        id=new AtomicInteger(Integer.parseInt(getId().get(getId().size()-1))+1);
        try{
            u.saveUploadFiles(file);
            u.persistMetaData(file,id.getAndAdd(1));
        }catch(IOException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("successfully uploaded!",new HttpHeaders(),HttpStatus.OK);
    }
    @GetMapping("/getFiles")
    public FilesMetadata getMetaData(@ModelAttribute FilesMetadata files){

        try {
            files.setFiles(f.findAllFiles());
        }catch(IOException e){
            e.getStackTrace();
        }
        return files;
    }
    @GetMapping("/getFiles/{filename}")
   public FileMetaData getFilesByFileName(@PathVariable String filename, FileMetaData file){
        try {
            if(f.findFilesByFileName(filename)==null) {
                file.setError("no such file");
                return file;
            }

            return f.findFilesByFileName(filename);

        }catch(IOException e){
            file.setError("IoException file!");
        }
        return null;
    }
    @GetMapping("/downLoad/{filename}")
    public void downLoadFile(HttpServletResponse response,@PathVariable String filename)throws IOException{
        String path = "/Users/min/Desktop/down/"+f.findFilesByFileName(filename).getFileName();
        File file = new File(f.findFilesByFileName(filename).getPath());
        String mimeType= "application/octet-stream";
        response.setContentType(mimeType);
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }
}
