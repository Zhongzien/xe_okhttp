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

    /**
     * 同步Post请求
     *
     * @return
     */
    void doPostSync(OnResultCallBack callBack);

    /**
     * 同步Get请求
     *
     * @return
     */
    void doGetSync(OnResultCallBack callBack);

    /**
     * 异步上传文件
     *
     * @return
     */
    void doUploadAsync(OnResultCallBack callBack);

    void doDownloadAsync();
}
