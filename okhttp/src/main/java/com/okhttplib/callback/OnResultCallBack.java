package com.okhttplib.callback;

import android.telecom.Call;

import com.okhttplib.HttpInfo;

import java.io.Serializable;

import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public interface OnResultCallBack extends Serializable {

    void onResponse(HttpInfo info);
}
