package com.jetsynthesys.rightlife.apimodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadImage {

    @SerializedName("fileName")
    @Expose
    private String fileName;
    @SerializedName("fileSize")
    @Expose
    private Long fileSize;
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

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
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