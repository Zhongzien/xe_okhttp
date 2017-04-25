package com.okhttplib.interceptor;

import com.okhttplib.HttpInfo;

/**
 * 信息拦截器
 * 对网络访问结果，进行拦截处理；例如：需要更明显的错误信息处理
 */

public interface MsgInterceptor {

    /**
     *
     * @param info
     */
    public void intercept(HttpInfo info);
}
