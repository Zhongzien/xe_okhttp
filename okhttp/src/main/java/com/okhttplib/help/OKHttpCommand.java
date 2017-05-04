package com.okhttplib.help;

import com.okhttplib.HttpInfo;
import com.okhttplib.annotation.RequestMethod;
import com.okhttplib.callback.FileObserver;
import com.okhttplib.config.Configuration;
import com.okhttplib.callback.OnResultCallBack;

import okhttp3.Request;

/**
 * 命令角色
 */

public class OKHttpCommand {

    private OnResultCallBack callBack;
    /**
     * 请求参数定义
     **/
    private HttpInfo info;

    private
    @RequestMethod
    int requestMethod;

    private OkHttpPerformer httpPerformer;

    private OkUploadPerformer okUploadPerformer;

    private OKHttpCommand(Builder builder) {
        info = builder.info;
        requestMethod = builder.requestMethod;
        callBack = builder.callBack;
        httpPerformer = new OkHttpPerformer(builder.config);

        if (info.getUploadFiles() != null && !info.getUploadFiles().isEmpty())
            okUploadPerformer = new OkUploadPerformer(builder.config);
    }

    public void doRequestAsync() {
        httpPerformer.doRequestAsync(this);
    }

    public void doRequestSync() {
        httpPerformer.doRequestSync(this);
    }

    public void doFileUploadAsync() {
        okUploadPerformer.doRequestAsync(this);
    }

    public void doFileUploadSync() {
        okUploadPerformer.doRequestSync(this);
    }

    public static Builder getCommandBuilder() {
        return new Builder();
    }

    public static class Builder {

        private OnResultCallBack callBack;
        private HttpInfo info;
        private Configuration config;
        private
        @RequestMethod
        int requestMethod;

        public Builder() {
        }

        public Builder setRequestMethod(@RequestMethod int method) {
            requestMethod = method;
            return this;
        }

        public Builder setOnResultCallBack(OnResultCallBack callBack) {
            this.callBack = callBack;
            return this;
        }

        public Builder setConfiguration(Configuration config) {
            this.config = config;
            return this;
        }

        public Builder setInvokerBuilder(HttpInfo info) {
            this.info = info;
            return this;
        }

        public OKHttpCommand build() {
            return new OKHttpCommand(this);
        }

    }

    public OnResultCallBack getCallBack() {
        return callBack;
    }

    public HttpInfo getInfo() {
        return info;
    }

    public FileObserver getFileObserver() {
        return okUploadPerformer;
    }

    public
    @RequestMethod
    int getRequestMethod() {
        return requestMethod;
    }
}
