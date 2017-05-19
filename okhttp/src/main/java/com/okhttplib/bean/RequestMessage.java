package com.okhttplib.bean;


import com.okhttplib.HttpInfo;
import com.okhttplib.callback.OnResultCallBack;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public final class RequestMessage extends BasicOkMessage {

    public OnResultCallBack callBack;
    public HttpInfo info;

    public RequestMessage(int resultCode, HttpInfo info, OnResultCallBack callBack) {
        what = resultCode;
        this.info = info;
        this.callBack = callBack;
    }

    public RequestMessage(){
    }

}
