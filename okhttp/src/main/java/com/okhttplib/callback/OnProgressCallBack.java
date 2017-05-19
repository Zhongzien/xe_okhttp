package com.okhttplib.callback;

/**
 * Created by Administrator on 2017/5/16 0016.
 */

public interface OnProgressCallBack extends OnResultCallBack {

    public void onProgress(long percent,long curLength,long totalLength);

}
