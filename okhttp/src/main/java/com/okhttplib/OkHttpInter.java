package com.okhttplib;

import com.okhttplib.callback.OnResultCallBack;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public interface OkHttpInter {

    /**
     * 异步post请求
     */
    void doPostAsync(OnResultCallBack callBack);

    /**
     * 异步get请求
     */
    void doGetAsync(OnResultCallBack callBack);

}
