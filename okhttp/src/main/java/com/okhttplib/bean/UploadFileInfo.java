package com.okhttplib.bean;

import java.io.Serializable;

/**
 * 文件上传/下载是用到的文件信息
 */

public class UploadFileInfo implements Serializable {

    //文件的绝对路径
    private String fileAbsolutePath;
    //上传的名称
    private String uploadFormat;

    public UploadFileInfo(String uploadFormat, String fileAbsolutePath) {
        this.uploadFormat = uploadFormat;
        this.fileAbsolutePath = fileAbsolutePath;
    }

    public String getFileAbsolutePath() {
        return fileAbsolutePath;
    }

    public void setFileAbsolutePath(String fileAbsolutePath) {
        this.fileAbsolutePath = fileAbsolutePath;
    }

    public String getUploadFormat() {
        return uploadFormat;
    }

    public void setUploadFormat(String uploadFormat) {
        this.uploadFormat = uploadFormat;
    }
}
