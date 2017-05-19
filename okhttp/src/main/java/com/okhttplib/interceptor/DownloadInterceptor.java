package com.okhttplib.interceptor;

import com.okhttplib.bean.DownloadFileInfo;
import com.okhttplib.progress.ProgressResponse;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/16 0016.
 */

public class DownloadInterceptor implements Interceptor {

    private DownloadFileInfo fileInfo;

    public DownloadInterceptor(DownloadFileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder().
                body(new ProgressResponse(response.body(), fileInfo)).
                build();
    }
}
