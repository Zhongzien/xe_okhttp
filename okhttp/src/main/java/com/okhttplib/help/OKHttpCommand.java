package com.okhttplib.help;

import com.okhttplib.HttpInfo;
import com.okhttplib.annotation.RequestMethod;
import com.okhttplib.config.Configuration;
import com.okhttplib.callback.OnResultCallBack;

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

    private OKHttpCommand(Builder builder) {
        info = builder.info;
        requestMethod = builder.requestMethod;
        callBack = builder.callBack;
        httpPerformer = new OkHttpPerformer(builder.config);
    }

    public void doRequestAsync() {
        httpPerformer.doRequestAsync(this);
    }

    public void doRequestSncy() {

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

        public Builder setRequestMehtod(@RequestMethod int method) {
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

    public
    @RequestMethod
    int getRequestMethod() {
        return requestMethod;
    }
}
