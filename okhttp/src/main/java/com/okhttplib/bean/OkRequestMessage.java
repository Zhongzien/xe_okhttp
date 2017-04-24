package com.okhttplib.bean;


import com.okhttplib.HttpInfo;
import com.okhttplib.callback.OnResultCallBack;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public final class OkRequestMessage extends BaseOkMessage {

    public OnResultCallBack callBack;
    public HttpInfo info;

    public OkRequestMessage(int resultCode, HttpInfo info, OnResultCallBack callBack) {
        what = resultCode;
        this.info = info;
        this.callBack = callBack;
    }

    public OkRequestMessage(){
    }

}
