package com.okhttplib.callback;

import android.telecom.Call;

import java.io.Serializable;

import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public interface OnResultCallBack extends Serializable {

    public void onSuccess(String result, String msg);

    public void onFailure(String msg);
}
