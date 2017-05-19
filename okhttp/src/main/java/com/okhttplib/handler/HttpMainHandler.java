package com.okhttplib.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.okhttplib.bean.ProgressMessage;
import com.okhttplib.bean.RequestMessage;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public class HttpMainHandler extends Handler {

    private static HttpMainHandler mHandler;

    /**
     * 网络请求回调
     */
    public final static int RESPONSE_HTTP = 0x01;

    public final static int PROGRESS_UPDATE = 0x02;

    public HttpMainHandler() {
        super(Looper.getMainLooper());
    }

    public final static HttpMainHandler getInstance() {
        if (mHandler == null) {
            synchronized (HttpMainHandler.class) {
                if (mHandler == null) {
                    mHandler = new HttpMainHandler();
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
                    RequestMessage rMsg = (RequestMessage) msg.obj;
                    if (rMsg.callBack != null)
                        rMsg.callBack.onResponse(rMsg.info);
                    break;
                case PROGRESS_UPDATE:
                    ProgressMessage pMsg = (ProgressMessage) msg.obj;
                    if (pMsg.callBack != null)
                        pMsg.callBack.onProgress(pMsg.percent, pMsg.curLength, pMsg.totalLength);
                    break;
            }
        } catch (Exception e) {
        }
    }


}
