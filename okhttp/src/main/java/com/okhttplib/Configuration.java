package com.okhttplib;

import android.content.Context;
import android.support.annotation.NonNull;

import com.okhttplib.interceptor.MediaTypeInterceptor;
import com.okhttplib.interceptor.MsgInterceptor;
import com.okhttplib.interceptor.ParamsInterceptor;
import com.okhttplib.utils.DefaultUploadInterceptor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.OkHttpClient;

/**
 * 全局配置文件
 */

public final class Configuration {

    private final static int DEFAULT_TIME_OUT = 15;

    private Builder mBuilder;
    private static OkHttpClient mHttpClient;

    private Configuration() {
    }

    private Configuration(Builder builder) {
        mBuilder = builder;
    }

    protected static Builder getDefBuilder() {
        return new Configuration.Builder();
    }

    public static Builder getConfigBuilder(Context context) {
        return new Configuration.Builder(context);
    }

    public final static class Builder {
        private long connectTimeOut;
        private long readTimeOut;
        private long writeTimeOut;
        private TimeUnit timeUnit;

        private File cacheFile;
        private long cacheMaxSize;

        private String saveDirPath;

        private List<MsgInterceptor> okHttpInterceptors;
        private ParamsInterceptor paramsInterceptor;
        private MediaTypeInterceptor mediaTypeInterceptor;

        private boolean isCookiesJar;
        private boolean isCookiesStrong;
        public Context context;

        private Builder() {
            initDefaultConfig();
        }

        private Builder(@NonNull Context context) {
            this.context = context;
            initDefaultConfig();
        }

        private void initDefaultConfig() {
            isCookiesJar = false;
            isCookiesStrong = false;
            addConnectTimeOut(DEFAULT_TIME_OUT);
            addReadTimeOut(DEFAULT_TIME_OUT);
            addWriteTimeOut(DEFAULT_TIME_OUT);
            addTimeUnit(TimeUnit.SECONDS);
            mediaTypeInterceptor = new DefaultUploadInterceptor();
        }

        private Builder addSaveDirPath(String dir) {
            saveDirPath = dir;
            return this;
        }

        public Builder addMadiaTypeInterceptor(MediaTypeInterceptor interceptor) {
            if (interceptor != null)
                mediaTypeInterceptor = interceptor;
            return this;
        }

        public Builder addCacheFile(File file) {
            if (file != null) {
                cacheFile = file;
                if (cacheMaxSize <= 0)
                    cacheMaxSize = 10 * 1024 * 1024;
            }
            return this;
        }

        public Builder addCacheSize(long size) {
            if (size > 0) {
                cacheMaxSize = size;
            }
            return this;
        }

        public Builder setCookiesStrong(boolean isCookiesStrong) {
            isCookiesJar = true;
            this.isCookiesStrong = isCookiesStrong;
            return this;
        }

        public Builder addConnectTimeOut(long connectTimeOut) {
            if (connectTimeOut <= 0)
                throw new IllegalArgumentException("connectTimeOut can not less than zero");
            this.connectTimeOut = connectTimeOut;
            return this;
        }

        public Builder addReadTimeOut(long readTimeOut) {
            if (readTimeOut <= 0)
                throw new IllegalArgumentException("readTimeOut can not less than zero");
            this.readTimeOut = readTimeOut;
            return this;
        }

        public Builder addWriteTimeOut(long writeTimeOut) {
            if (readTimeOut <= 0)
                throw new IllegalArgumentException("writeTimeOut can not less than zero");
            this.writeTimeOut = writeTimeOut;
            return this;
        }

        public Builder addTimeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
            return this;
        }

        public Builder addParamsInterceptor(ParamsInterceptor interceptor) {
            if (interceptor != null)
                paramsInterceptor = interceptor;
            return this;
        }

        public Builder addMsgInterceptor(MsgInterceptor interceptor) {
            if (interceptor != null) {
                if (okHttpInterceptors == null)
                    okHttpInterceptors = new ArrayList<>();
                okHttpInterceptors.add(interceptor);
            }
            return this;
        }

        public void bindConfig() {
            OkHttpInvoker.config(new Configuration(this));
        }

    }

    public File getCacheFile() {
        return mBuilder.cacheFile;
    }

    public long getCacheMaxSize() {
        return mBuilder.cacheMaxSize;
    }

    public long getConnectTimeOut() {
        return mBuilder.connectTimeOut;
    }

    public long getReadTimeOut() {
        return mBuilder.readTimeOut;
    }

    public long getWriteTimeOut() {
        return mBuilder.writeTimeOut;
    }

    public TimeUnit getTimeUnit() {
        return mBuilder.timeUnit;
    }

    public ParamsInterceptor getParamsInterceptor() {
        return mBuilder.paramsInterceptor;
    }

    public List<MsgInterceptor> getMsgInterceptor() {
        return mBuilder.okHttpInterceptors;
    }

    public MediaTypeInterceptor getMediaTypeInterceptor() {
        return mBuilder.mediaTypeInterceptor;
    }

    public void setOkHttpClient(OkHttpClient client) {
        mHttpClient = client;
    }

    public OkHttpClient getOkHttpClient() {
        return mHttpClient;
    }

    public String getSaveDirPath() {
        return mBuilder.saveDirPath;
    }

    public boolean isCookiesJar() {
        return mBuilder.isCookiesJar;
    }

    public boolean isCookieStrong() {
        return mBuilder.isCookiesStrong;
    }

    public Context getContext() {
        return mBuilder.context;
    }

}
