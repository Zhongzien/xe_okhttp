package com.okhttplib.bean;

import com.okhttplib.annotation.DownloadStatus;
import com.okhttplib.callback.OnProgressCallBack;

/**
 * Created by Administrator on 2017/5/15 0015.
 */

public class DownloadFileInfo {

    private String url;
    private String saveDir;
    private String saveFileName;

    private OnProgressCallBack mCallBack;

    @DownloadStatus
    private int status;

    private long completeSize;

    /**
     * 下载时候使用URL的数字签名作名称，避免重名覆盖的现象*
     */
    private String saveSignaturFileName;
    /**
     * 带扩展名的文件名称，用于下载成功后把密文的文件名修改为明文的名称
     */
    private String saveFileNameWithExtension;
    /**
     * 用于出现重名现象是重命名，防止文件覆盖
     */
    private String saveCopeFileNameWithExtension;

    public DownloadFileInfo(String url, String saveDir, String saveFileName, OnProgressCallBack callBack) {
        this.url = url;
        this.saveDir = saveDir;
        this.saveFileName = saveFileName;
        this.mCallBack = callBack;
    }

    public String getSaveDir() {
        return saveDir;
    }

    public void setSaveDir(String saveDir) {
        this.saveDir = saveDir;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }

    public OnProgressCallBack getOnProgressCallBack() {
        return mCallBack;
    }

    public void setOnProgressCallBack(OnProgressCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public String getSaveSignaturFileName() {
        return saveSignaturFileName;
    }

    public void setSaveSignaturFileName(String saveSignaturFileName) {
        this.saveSignaturFileName = saveSignaturFileName;
    }

    public String getSaveFileNameWithExtension() {
        return saveFileNameWithExtension;
    }

    public void setSaveFileNameWithExtension(String saveFileNameWithExtension) {
        this.saveFileNameWithExtension = saveFileNameWithExtension;
    }

    public String getSaveCopeFileNameWithExtension() {
        return saveCopeFileNameWithExtension;
    }

    public void setSaveCopeFileNameWithExtension(String saveCopeFileNameWithExtension) {
        this.saveCopeFileNameWithExtension = saveCopeFileNameWithExtension;
    }

    public long getCompleteSize() {
        return completeSize;
    }

    public void setCompleteSize(long completeSize) {
        this.completeSize = completeSize;
    }

    public
    @DownloadStatus
    int getDownloadStatus() {
        return status;
    }

    public void setDownloadStatus(@DownloadStatus int status) {
        this.status = status;
    }
}
