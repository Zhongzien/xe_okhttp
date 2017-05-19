package com.okhttplib;

import android.text.TextUtils;

import com.okhttplib.bean.DownloadFileInfo;
import com.okhttplib.bean.UploadFileInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 访问相关信息，包括访问和返回信息
 */

public class HttpInfo {

    private HttpInfo() {
    }

    public static HttpInfo getInstance() {
        return new HttpInfo();
    }

    //普通请求
    private HashMap<String, String> params;
    private String url;
    private HashMap<String, String> heads;

    public HttpInfo addHeads(HashMap<String, String> heads) {
        this.heads = heads;
        return this;
    }

    public HttpInfo addHead(String key, String value) {
        if (key != null) {
            if (heads == null) heads = new HashMap<>();
            heads.put(key, value);
        }

        return this;
    }

    public HttpInfo addParams(HashMap<String, String> params) {
        this.params = params;
        return this;
    }

    public HttpInfo addUrl(String url) {
        this.url = url;
        return this;
    }

    public HashMap<String, String> getHeads() {
        return heads;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public String getUrl() {
        return url;
    }

    //文件上传下载
    private List<UploadFileInfo> uploadFiles;

    public List<UploadFileInfo> getUploadFiles() {
        return uploadFiles;
    }

    public HttpInfo addUploadFiles(List<UploadFileInfo> files) {
        uploadFiles = files;
        return this;
    }

    //文件下载

    private DownloadFileInfo downloadFile;

    public DownloadFileInfo getDownloadFile() {
        return downloadFile;
    }

    public HttpInfo addDownloadFile(DownloadFileInfo info) {
        downloadFile = info;
        return this;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //*****本地状态码*****/
    //基础信息
    public final static int NET_SUCCESS = 0x00;
    public final static int NET_FAILURE = 0x01;
    public final static int CHECK_URL = 0x02;
    public final static int CLIENT_4XX = 0x03;
    public final static int SERVICE_5XX = 0x04;
    public final static int NETWORK_ON_MAIN_THREAD = 0x05;
    public final static int CONNECTION_TIME_OUT = 0x06;
    public final static int READ_OR_WRITE_TIME_OUT = 0x07;
    public final static int ON_RESULT = 0x08;
    public final static int CONNECTION_INTERRUPTION = 0x09;

    //下载信息
    public final static int DOWNLOAD_PAUSE = 0x10;
    public final static int DOWNLOAD_STOP = 0x11;
    public final static int DOWNLOAD_COMPLETE = 0x12;

    //返回参数
    private String msg;
    // body and netCode return from NetWork and localCode is flag they can't change
    private String resultBody;
    private int netCode;
    private int localCode;

    public void packInfo(int netCode, int localCode, String body) {
        this.netCode = netCode;
        this.localCode = localCode;
        switch (localCode) {
            case NET_SUCCESS:
                msg = "发送请求成功";
                break;
            case NET_FAILURE:
                msg = "发送请求失败";
                break;
            case CHECK_URL:
                msg = "访问协议类型异常";
                break;
            case CLIENT_4XX:
                msg = "客户端访问异常";
                break;
            case SERVICE_5XX:
                msg = "服务器异常";
                break;
            case NETWORK_ON_MAIN_THREAD:
                msg = "不允许在UI线程中进行网络操作";
                break;
            case CONNECTION_TIME_OUT:
                msg = "连接超时";
                break;
            case READ_OR_WRITE_TIME_OUT:
                msg = "读写超时";
                break;
            case ON_RESULT:
                msg = "没有获取到数据";
                break;
            case CONNECTION_INTERRUPTION:
                msg = "连接中断";
                break;
            case DOWNLOAD_PAUSE:
                msg = "暂停下载";
                break;
            case DOWNLOAD_STOP:
                msg = "停止下载";
                break;
            case DOWNLOAD_COMPLETE:
                msg = "完成下载";
                break;
        }
        resultBody = body == null ? "" : body;
    }


    public int getNetCode() {
        return netCode;
    }

    public int getLocalCode() {
        return localCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResultBody() {
        return resultBody;
    }

    public boolean isSuccess() {
        return localCode == NET_SUCCESS;
    }
}
