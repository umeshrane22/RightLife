package com.example.rlapp.apimodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.processing.Generated;

@Generated("jsonschema2pojo")
public class UploadImage {

    @SerializedName("fileName")
    @Expose
    private String fileName;
    @SerializedName("fileSize")
    @Expose
    private Integer fileSize;
    @SerializedName("fileType")
    @Expose
    private String fileType;
    @SerializedName("isPublic")
    @Expose
    private Boolean isPublic;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

}