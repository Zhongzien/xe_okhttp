package com.okhttplib.help;

import android.os.Message;
import android.util.Log;

import com.okhttplib.HttpInfo;
import com.okhttplib.annotation.RequestMethod;
import com.okhttplib.config.Configuration;
import com.okhttplib.bean.OkRequestMessage;
import com.okhttplib.callback.OnResultCallBack;
import com.okhttplib.handler.OkHttpMainHandler;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class OkHttpPerformer extends BasicOkPerformer {

    OkHttpPerformer(Configuration config) {
        super(config);
    }

    @Override
    public void doRequestAsync(OKHttpCommand command) {
        final OnResultCallBack callBack = command.getCallBack();
        if (callBack == null)
            throw new NullPointerException("OnResultCallBack can not null!");
        final HttpInfo info = command.getInfo();
        if (!checkUrl(info.getUrl())) {
            Message msg = new OkRequestMessage(OkHttpMainHandler.RESPONSE_HTTP,
                    updateInfo(info, HttpInfo.CHECK_URL),
                    callBack).build();
            OkHttpMainHandler.getInstance().sendMessage(msg);
            return;
        }
        final Request request = buildRequest(command.getInfo(), command.getRequestMethod());
        Call call = getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = new OkRequestMessage(OkHttpMainHandler.RESPONSE_HTTP,
                        updateInfo(info, HttpInfo.NET_FAILURE, e.getMessage()),
                        callBack).build();
                OkHttpMainHandler.getInstance().sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = new OkRequestMessage(OkHttpMainHandler.RESPONSE_HTTP,
                        doResponse(info, response),
                        callBack).build();
                OkHttpMainHandler.getInstance().sendMessage(msg);
                if (!call.isCanceled()) call.cancel();
            }
        });
    }

    @Override
    public void doRequestSync() {
    }

    private HttpInfo doResponse(HttpInfo info, Response res) {
        try {
            if (res != null) {
                String body = res.body().string();
                if (res.isSuccessful()) {
                    return updateInfo(info, res.code(), HttpInfo.NET_SUCCESS, body);
                } else {
                    if (res.code() >= 400 && res.code() < 500) {
                        return updateInfo(info, res.code(), HttpInfo.CLIENT_4XX, body);
                    } else if (res.code() >= 500 && res.code() < 600) {
                        return updateInfo(info, res.code(), HttpInfo.SERVICE_5XX, body);
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

    private HttpInfo updateInfo(HttpInfo info, int localCode) {
        // has no netCode and body
        return updateInfo(info, localCode, localCode, null);
    }

    private HttpInfo updateInfo(HttpInfo info, int localCode, String body) {
        // has no netCode
        return updateInfo(info, localCode, localCode, body);
    }

    private HttpInfo updateInfo(HttpInfo info, int netCode, int localCode, String body) {
        info.packInfo(netCode, localCode, body);
        dealInterceptor(info);
        return info;
    }

    void setRequestHeads(HttpInfo info, Request.Builder builder) {
        if (info.getHeads() != null && !info.getHeads().isEmpty()) {
            for (String key : info.getHeads().keySet()) {
                builder.addHeader(key, info.getHeads().get(key));
            }
        }
    }

    private Request buildRequest(HttpInfo info, @RequestMethod int method) {
        Request.Builder builder = new Request.Builder();
        switch (method) {
            case RequestMethod.POST:
                FormBody.Builder fBuilder = new FormBody.Builder();
                if (info.getParams() != null && !info.getParams().isEmpty()) {
                    for (String key : info.getParams().keySet()) {
                        Object param = info.getParams().get(key);
                        String value = param == null ? "" : String.valueOf(param);
                        fBuilder.add(key, value);
                    }
                }
                builder.url(info.getUrl()).post(fBuilder.build());
                Log.i("OkHttp", "POST");
                break;
            case RequestMethod.GET:
                StringBuilder urlIntegral = new StringBuilder();
                urlIntegral.append(info.getUrl());
                if (info.getParams() != null && !info.getParams().isEmpty()) {
                    if (urlIntegral.indexOf("?") < 0)
                        urlIntegral.append("?");
                    boolean isFirst = urlIntegral.toString().endsWith("?");
                    for (String key : info.getParams().keySet()) {
                        Object param = info.getParams().get(key);
                        String value = param == null ? "" : String.valueOf(param);
                        if (isFirst) {
                            urlIntegral.append(key).append("=").append(value);
                            isFirst = false;
                        } else {
                            urlIntegral.append("&").append(key).append("=").append(value);
                        }
                    }
                }
                builder.url(urlIntegral.toString()).get();
                Log.i("OkHttp", "GET");
                break;
            default:
                builder.url(info.getUrl()).get();
                Log.i("OkHttp", "default");
                break;
        }
        setRequestHeads(info, builder);
        return builder.build();
    }
}
