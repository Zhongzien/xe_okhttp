package com.okhttplib.help;

import android.text.TextUtils;

import com.okhttplib.HttpInfo;
import com.okhttplib.config.Configuration;
import com.okhttplib.interceptor.MediaTypeInterceptor;
import com.okhttplib.interceptor.MsgInterceptor;
import com.okhttplib.interceptor.ParamsInterceptor;

import java.util.List;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 命令执行者基类
 */

public abstract class BasicPerformer {

    private OkHttpClient mHttpClient;

    protected final List<MsgInterceptor> okHttpInterceptors;
    protected final ParamsInterceptor paramsInterceptor;
    protected final MediaTypeInterceptor mediaTypeInterceptor;

    BasicPerformer(Configuration config) {
        okHttpInterceptors = config.getMsgInterceptor();
        paramsInterceptor = config.getParamsInterceptor();
        mediaTypeInterceptor = config.getMediaTypeInterceptor();

        mHttpClient = buildOkHttpClient(config);
    }

    protected OkHttpClient buildOkHttpClient(Configuration config) {
        return null;
    }

    protected OkHttpClient.Builder newOkHttpClientBuilder(Configuration config) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().
                connectTimeout(config.getConnectTimeOut(), config.getTimeUnit()).
                readTimeout(config.getReadTimeOut(), config.getTimeUnit()).
                writeTimeout(config.getWriteTimeOut(), config.getTimeUnit());

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
            for (MsgInterceptor interceptor : okHttpInterceptors) {
                interceptor.intercept(info);
            }
        }
    }

    protected HttpInfo doResponse(HttpCommand command, Response res) {
        final HttpInfo info = command.getInfo();
        try {
            if (res != null) {
                if (res.isSuccessful()) {
                    if (command.getPerformer() != null) {
                        return command.getPerformer().downloadInfo(info, res, null);
                    } else {
                        return updateInfo(info, res.code(), HttpInfo.NET_SUCCESS, res.body().string());
                    }
                } else {
                    if (res.code() >= 400 && res.code() < 500) {
                        return updateInfo(info, res.code(), HttpInfo.CLIENT_4XX, res.body().string());
                    } else if (res.code() >= 500 && res.code() < 600) {
                        return updateInfo(info, res.code(), HttpInfo.SERVICE_5XX, res.body().string());
                    }
                }
            }
            return updateInfo(info, HttpInfo.CHECK_URL);
        } catch (Exception e) {
            return updateInfo(info, HttpInfo.NET_FAILURE, "[" + e.getMessage() + "]");
        } finally {
            res.close();
        }
    }

    protected HttpInfo updateInfo(HttpInfo info, int localCode) {
        // has no netCode and body
        return updateInfo(info, localCode, localCode, null);
    }

    protected HttpInfo updateInfo(HttpInfo info, int localCode, String body) {
        // has no netCode
        return updateInfo(info, localCode, localCode, body);
    }

    protected HttpInfo updateInfo(HttpInfo info, int netCode, int localCode, String body) {
        info.packInfo(netCode, localCode, body);
        dealInterceptor(info);
        return info;
    }

    protected void setRequestHeads(HttpInfo info, Request.Builder builder) {
        if (info.getHeads() != null && !info.getHeads().isEmpty()) {
            for (String key : info.getHeads().keySet()) {
                builder.addHeader(key, info.getHeads().get(key));
            }
        }
    }

    protected void doRequestAsync(HttpCommand command) {
    }

    protected void doRequestSync(HttpCommand command) {
    }

    protected HttpInfo downloadInfo(HttpInfo info, Response response, Call call) {
        return null;
    }
}
