package com.okhttplib.help;

import com.okhttplib.HttpInfo;
import com.okhttplib.annotation.DownloadStatus;
import com.okhttplib.annotation.RequestMethod;
import com.okhttplib.callback.OnResultCallBack;
import com.okhttplib.config.Configuration;

/**
 * 命令角色
 */

public final class HttpCommand {

    private OnResultCallBack callBack;
    /**
     * 请求参数定义
     **/
    private HttpInfo info;

    private
    @RequestMethod
    int requestMethod;

    private BasicPerformer httpPerformer;

    private BasicPerformer downOrUp;

    private HttpCommand(Builder builder) {
        info = builder.info;
        requestMethod = builder.requestMethod;
        callBack = builder.callBack;
        httpPerformer = new HttpPerformer(builder.config);

        if (info.getUploadFiles() != null && !info.getUploadFiles().isEmpty())
            downOrUp = new UploadPerformer(builder.config);

        if (info.getDownloadFile() != null)
            downOrUp = new DownloadPerformer(builder.config);
    }

    public void doRequestAsync() {
        httpPerformer.doRequestAsync(this);
    }

    public void doRequestSync() {
        httpPerformer.doRequestSync(this);
    }

    public void doFileUploadAsync() {
        downOrUp.doRequestAsync(this);
    }

    @Deprecated
    public void doFileUploadSync() {
        downOrUp.doRequestSync(this);
    }

    public void doDownloadAsync() {
        downOrUp.doRequestAsync(this);
    }

    public static Builder getBuilder() {
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

        public HttpCommand build() {
            return new HttpCommand(this);
        }

    }

    public OnResultCallBack getCallBack() {
        return callBack;
    }

    void setCallBack(OnResultCallBack callBack) {
        this.callBack = callBack;
    }

    public HttpInfo getInfo() {
        return info;
    }

    public BasicPerformer getPerformer() {
        return downOrUp;
    }

    public
    @RequestMethod
    int getRequestMethod() {
        return requestMethod;
    }

    public static void updateDownloadStatus(String key, @DownloadStatus int status) {
        DownloadPerformer.updateDownloadStatus(key, status);
    }
}
