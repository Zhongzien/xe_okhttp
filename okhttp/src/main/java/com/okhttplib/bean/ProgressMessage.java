package com.okhttplib.bean;

import com.okhttplib.callback.OnProgressCallBack;

/**
 * Created by Administrator on 2017/5/16 0016.
 */

public class ProgressMessage extends BasicOkMessage {
    public OnProgressCallBack callBack;
    public long percent;
    public long curLength;
    public long totalLength;

    public ProgressMessage(int what, long percent, long curLength, long totalLength, OnProgressCallBack callBack) {
        this.what = what;
        this.callBack = callBack;
        this.percent = percent;
        this.curLength = curLength;
        this.totalLength = totalLength;
    }
}
