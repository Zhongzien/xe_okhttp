package com.okhttplib;

import com.okhttplib.annotation.RequestMethod;
import com.okhttplib.bean.UploadFileInfo;
import com.okhttplib.config.Configuration;
import com.okhttplib.callback.OnResultCallBack;
import com.okhttplib.help.OKHttpCommand;

import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;

/**
 * 命令请求者角色
 */

public class OkHttpInvoker implements OkHttpInter {

    private static Configuration mConfig;

    private Builder mBuilder;

    static {
        config(new Configuration.Builder().build());
    }

    private OkHttpInvoker(Builder builder) {
        mBuilder = builder;
    }

    @Override
    public void doPostAsync(final OnResultCallBack callBack) {
        OKHttpCommand.getCommandBuilder().
                setConfiguration(mConfig).
                setInvokerBuilder(mBuilder.info).
                setOnResultCallBack(callBack).
                setRequestMethod(RequestMethod.POST).
                build().doRequestAsync();
    }

    @Override
    public void doGetAsync(final OnResultCallBack callBack) {
        OKHttpCommand.getCommandBuilder().
                setConfiguration(mConfig).
                setInvokerBuilder(mBuilder.info).
                setOnResultCallBack(callBack).
                setRequestMethod(RequestMethod.GET).
                build().doRequestAsync();
    }

    @Override
    public void doPostSync(OnResultCallBack callBack) {
        OKHttpCommand.getCommandBuilder().
                setConfiguration(mConfig).
                setInvokerBuilder(mBuilder.info).
                setOnResultCallBack(callBack).
                setRequestMethod(RequestMethod.POST).
                build().doRequestSync();
    }

    @Override
    public void doGetSync(OnResultCallBack callBack) {
        OKHttpCommand.getCommandBuilder().
                setConfiguration(mConfig).
                setInvokerBuilder(mBuilder.info).
                setOnResultCallBack(callBack).
                setRequestMethod(RequestMethod.GET).
                build().doRequestSync();
    }

    @Override
    public void doUploadAsync(OnResultCallBack callBack) {
        OKHttpCommand.getCommandBuilder().
                setConfiguration(mConfig).
                setInvokerBuilder(mBuilder.info).
                setOnResultCallBack(callBack).
                build().doFileUploadAsync();
    }

    @Override
    public void doUploadSync(OnResultCallBack callBack) {
        OKHttpCommand.getCommandBuilder().
                setConfiguration(mConfig).
                setInvokerBuilder(mBuilder.info).
                setOnResultCallBack(callBack).
                build().doRequestSync();
    }

    public static void config(Configuration config) {
        mConfig = config;
    }

    public static Builder getDefaultBuilder() {
        return new Builder();
    }

    public static class Builder {

        private HttpInfo info;

        public Builder() {
            info = new HttpInfo();
        }

        public Builder setUrl(String url) {
            info.setUrl(url);
            return this;
        }

        public Builder addParams(HashMap<String, String> params) {
            info.addParams(params);
            return this;
        }

        public Builder addParam(String key, String value) {
            info.addParam(key, value);
            return this;
        }

        public Builder addHeads(HashMap<String, String> heads) {
            info.addHeads(heads);
            return this;
        }

        public Builder addHead(String key, String value) {
            info.addHead(key, value);
            return this;
        }

        public Builder addUploadFiles(List<UploadFileInfo> files) {
            info.setUploadFiles(files);
            return this;
        }

        public Builder addUploadFile(String uploadFormat, String fileAbsolutePath) {
            info.setUploadFiles(uploadFormat, fileAbsolutePath);
            return this;
        }

        public OkHttpInter build() {
            return new OkHttpInvoker(this);
        }

    }

    public void test() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
    }
}
