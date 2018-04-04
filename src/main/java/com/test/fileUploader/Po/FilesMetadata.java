package com.test.fileUploader.Po;

import java.util.List;

public class FilesMetadata {
    private List<FileMetaData> f;
    public List<FileMetaData> getFiles() {
        return f;
    }

    public void setFiles(List<FileMetaData> files) {
        this.f = files;
    }
}
