package com.okhttplib.interceptor;

import com.okhttplib.HttpInfo;

/**
 * Created by Administrator on 2017/4/21 0021.
 */

public interface OkHttpInterceptor {
    public void intercept(HttpInfo info);
}
