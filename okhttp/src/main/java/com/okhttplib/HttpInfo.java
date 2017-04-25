package com.okhttplib;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * 访问相关信息，包括访问和返回信息
 */

public class HttpInfo {

    public HttpInfo() {
    }

    private HashMap<String, String> params;
    private String url;
    private HashMap<String, String> heads;

    public HashMap<String, String> getHeads() {
        return heads;
    }

    public void addHeads(HashMap<String, String> heads) {
        if (heads != null) {
            if (this.heads == null)
                this.heads = new HashMap<>();
            this.heads.putAll(heads);
        }
    }

    public void addHead(String key, String value) {
        if (!TextUtils.isEmpty(key)) {
            if (heads == null)
                heads = new HashMap<>();
            value = value == null ? "" : value;
            heads.put(key, value);
        }
    }

    public void addParams(HashMap<String, String> params) {
        if (params != null) {
            if (this.params == null)
                this.params = new HashMap<>();
            this.params.putAll(params);
        }
    }

    public void addParam(String key, String value) {
        if (!TextUtils.isEmpty(key)) {
            if (params == null)
                params = new HashMap<>();
            value = value == null ? "" : value;
            params.put(key, value);
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public String getUrl() {
        return url;
    }

    //*****本地状态码*****/
    public final static int NET_SUCCESS = 0x00;
    public final static int NET_FAILURE = 0x01;
    public final static int CHECK_URL = 0x02;
    public final static int CLIENT_4XX = 0x03;
    public final static int SERVICE_5XX = 0x04;
    public final static int NETWORK_ON_MAIN_THREAD = 0x05;
    public final static int CONNECTION_TIME_OUT = 0x06;
    public final static int READ_OR_WRITE_TIME_OUT = 0x07;
    public final static int ON_RESULT = 0x08;

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
