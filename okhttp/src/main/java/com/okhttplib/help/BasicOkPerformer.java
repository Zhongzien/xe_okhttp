package com.okhttplib.help;

import android.text.TextUtils;

import com.okhttplib.HttpInfo;
import com.okhttplib.config.Configuration;
import com.okhttplib.interceptor.OkHttpInterceptor;

import java.util.List;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * 抽象命令执行者
 */

public abstract class BasicOkPerformer {

    private static OkHttpClient mHttpClient;

    private List<OkHttpInterceptor> okHttpInterceptors;

    BasicOkPerformer(Configuration config) {
        okHttpInterceptors = config.getOkHttpInterceptors();
        newOkHttpClient(config);
    }

    private OkHttpClient newOkHttpClient(Configuration config) {
        if (mHttpClient == null) {
            synchronized (BasicOkPerformer.class) {
                if (mHttpClient == null)
                    mHttpClient = newOkHttpClientBuilder(config).build();
            }
        }
        return mHttpClient;
    }

    private OkHttpClient.Builder newOkHttpClientBuilder(Configuration config) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().
                connectTimeout(config.getConnectTimeOut(), config.getTimeUnit()).
                readTimeout(config.getReadTimeOut(), config.getTimeUnit()).
                writeTimeout(config.getWriteTimeOut(), config.getTimeUnit());
//        if (config.getCacheFile() != null) {
//            builder.cache(new Cache(config.getCacheFile(), config.getCacheMaxSize()));
//        }
        return builder;
    }

    protected OkHttpClient getOkHttpClient() {
        return mHttpClient;
    }

    boolean checkUrl(String url) {
        HttpUrl httpUrl = HttpUrl.parse(url);
        return httpUrl != null && !TextUtils.isEmpty(url);
    }

    protected void dealInterceptor(HttpInfo info) {
        if (okHttpInterceptors != null) {
            for (OkHttpInterceptor interceptor : okHttpInterceptors) {
                interceptor.intercept(info);
            }
        }
    }

    public abstract void doRequestAsync(OKHttpCommand command);

    public abstract void doRequestSync();

}
