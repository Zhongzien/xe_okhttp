package com.okhttplib.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.okhttplib.bean.OkRequestMessage;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public class OkHttpMainHandler extends Handler {

    private static OkHttpMainHandler mHandler;

    /**
     * 网络请求回调
     */
    public final static int RESPONSE_HTTP = 0x01;

    public OkHttpMainHandler() {
        super(Looper.getMainLooper());
    }

    public final static OkHttpMainHandler getInstance() {
        if (mHandler == null) {
            synchronized (OkHttpMainHandler.class) {
                if (mHandler == null) {
                    mHandler = new OkHttpMainHandler();
                }
            }
        }
        return mHandler;
    }

    @Override
    public void handleMessage(Message msg) {
        try {
            switch (msg.what) {
                case RESPONSE_HTTP:
                    OkRequestMessage rMsg = (OkRequestMessage) msg.obj;
                    if (rMsg.info.isSuccess()) {
                        rMsg.callBack.onSuccess(rMsg.info.getResultBody(), rMsg.info.getMsg());
                    } else {
                        rMsg.callBack.onFailure(rMsg.info.getMsg());
                    }
                    break;
            }
        } catch (Exception e) {
        }
    }


}
