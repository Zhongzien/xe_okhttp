package com.okhttplib.callback;

import com.okhttplib.HttpInfo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public interface OnResultCallBack extends Serializable {

    void onResponse(HttpInfo info);
}
