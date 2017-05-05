package com.okhttplib.help;

import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.okhttplib.HttpInfo;
import com.okhttplib.annotation.RequestMethod;
import com.okhttplib.help.inter.NetWorkInter;
import com.okhttplib.help.inter.RequestBuildInter;
import com.okhttplib.config.Configuration;
import com.okhttplib.bean.OkRequestMessage;
import com.okhttplib.callback.OnResultCallBack;
import com.okhttplib.handler.OkHttpMainHandler;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络请求执行者
 * 负责普通的网络请求访问：get/post
 * 分别提供同步和异步请求
 */

public class OkHttpPerformer extends BasicOkPerformer implements NetWorkInter {

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
        Call call = getOkHttpClient().newCall(checkRequest(command));
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
    public void doRequestSync(OKHttpCommand command) {
        final OnResultCallBack callBack = command.getCallBack();
        if (callBack == null)
            throw new NullPointerException("OnResultCallBack can not null!");
        final HttpInfo info = command.getInfo();
        if (!checkUrl(info.getUrl())) {
            callBack.onResponse(updateInfo(info, HttpInfo.CHECK_URL));
            return;
        }
        try {
            Call call = getOkHttpClient().newCall(checkRequest(command));
            Response response = call.execute();
            doResponse(info, response);
        } catch (SocketTimeoutException e) {
            if (null != e.getMessage()) {
                if (e.getMessage().contains("failed to connect to"))
                    updateInfo(info, HttpInfo.CONNECTION_TIME_OUT);
                if (e.getMessage().equals("timeout"))
                    updateInfo(info, HttpInfo.READ_OR_WRITE_TIME_OUT);
            }
            updateInfo(info, HttpInfo.READ_OR_WRITE_TIME_OUT);
        } catch (NetworkOnMainThreadException e) {
            updateInfo(info, HttpInfo.NETWORK_ON_MAIN_THREAD);
        } catch (Exception e) {
            updateInfo(info, HttpInfo.ON_RESULT);
        } finally {
            callBack.onResponse(info);
        }
    }

    private Request checkRequest(OKHttpCommand command) {
        RequestBuildInter observer = command.getFileObserver();
        Request request = null;
        if (observer != null) {
            request = observer.buildFileRequest(command.getInfo());
        }
        return request == null ? buildRequest(command.getInfo(), command.getRequestMethod()) : request;
    }

    private Request buildRequest(HttpInfo info, @RequestMethod int method) {
        Request.Builder builder = new Request.Builder();
        HashMap<String, String> params = info.getParams();
        if (paramsInterceptor != null) {
            //使用clone技术(原型模式)目的是防止原始数据被篡改
            params = paramsInterceptor.intercept(params == null ? null : (HashMap<String, String>) params.clone());
        }
        switch (method) {
            case RequestMethod.POST:
                FormBody.Builder fBuilder = new FormBody.Builder();
                if (params != null && !params.isEmpty()) {
                    for (String key : params.keySet()) {
                        String value = params.get(key);
                        value = value == null ? "" : value;
                        fBuilder.add(key, value);
                    }
                }
                builder.url(info.getUrl()).post(fBuilder.build());
                Log.i("OkHttp", "POST");
                break;
            case RequestMethod.GET:
                StringBuilder urlIntegral = new StringBuilder();
                urlIntegral.append(info.getUrl());
                if (params != null && !params.isEmpty()) {
                    if (urlIntegral.indexOf("?") < 0)
                        urlIntegral.append("?");
                    boolean isFirst = urlIntegral.toString().endsWith("?");
                    for (String key : params.keySet()) {
                        String value = params.get(key);
                        value = value == null ? "" : value;
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
        //配置http head
        setRequestHeads(info, builder);
        return builder.build();
    }
}
