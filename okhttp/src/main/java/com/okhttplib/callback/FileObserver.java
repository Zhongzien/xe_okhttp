package com.okhttplib.callback;

import com.okhttplib.HttpInfo;

import okhttp3.Request;

/**
 * Created by Administrator on 2017/5/3 0003.
 */

public interface FileObserver {

    Request getFileRequest(HttpInfo info);

}
