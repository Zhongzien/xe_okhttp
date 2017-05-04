package com.okhttplib.config;

import com.okhttplib.interceptor.MediaTypeInterceptor;
import com.okhttplib.interceptor.MsgInterceptor;
import com.okhttplib.interceptor.ParamsInterceptor;
import com.okhttplib.utils.DefaultUploadInterceptor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;

/**
 * 全局配置文件
 */

public final class Configuration implements Cloneable {

    private final static int DEFAULT_TIME_OUT = 15;

    private Builder mBuilder;

    private Configuration() {
    }

    @Override
    public Configuration clone() {
        try {
            Configuration config = (Configuration) super.clone();
            config.mBuilder = mBuilder.clone();
            return config;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Configuration(Builder builder) {
        mBuilder = builder;
    }

    public static Builder getConfigBulider() {
        return new Configuration.Builder();
    }

    public final static class Builder implements Cloneable {
        private long connectTimeOut;
        private long readTimeOut;
        private long writeTimeOut;
        private TimeUnit timeUnit;

        private File cacheFile;
        private long cacheMaxSize;

        private List<MsgInterceptor> okHttpInterceptors;
        private ParamsInterceptor paramsInterceptor;
        private MediaTypeInterceptor mediaTypeInterceptor;

        public Builder() {
            initDefaultConfig();
        }

        @Override
        public Builder clone() {
            try {
                Builder builder = (Builder) super.clone();
                builder.connectTimeOut = connectTimeOut;
                builder.readTimeOut = readTimeOut;
                builder.timeUnit = timeUnit;
                return builder;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        private void initDefaultConfig() {
            setConnectTimeOut(DEFAULT_TIME_OUT);
            setReadTimeOut(DEFAULT_TIME_OUT);
            setWriteTimeOut(DEFAULT_TIME_OUT);
            setTimeUnit(TimeUnit.SECONDS);
            mediaTypeInterceptor = new DefaultUploadInterceptor();
        }

        public Builder addMadiaTypeInterceptor(MediaTypeInterceptor interceptor) {
            if (interceptor != null)
                mediaTypeInterceptor = interceptor;
            return this;
        }

        public Builder setCacheFile(File file) {
            if (file != null) {
                cacheFile = file;
                if (cacheMaxSize <= 0)
                    cacheMaxSize = 10 * 1024 * 1024;
            }
            return this;
        }

        public Builder setCacheSize(long size) {
            if (size > 0) {
                cacheMaxSize = size;
            }
            return this;
        }

        public Builder setConnectTimeOut(long connectTimeOut) {
            if (connectTimeOut <= 0)
                throw new IllegalArgumentException("connectTimeOut can not less than zero");
            this.connectTimeOut = connectTimeOut;
            return this;
        }

        public Builder setReadTimeOut(long readTimeOut) {
            if (readTimeOut <= 0)
                throw new IllegalArgumentException("readTimeOut can not less than zero");
            this.readTimeOut = readTimeOut;
            return this;
        }

        public Builder setWriteTimeOut(long writeTimeOut) {
            if (readTimeOut <= 0)
                throw new IllegalArgumentException("writeTimeOut can not less than zero");
            this.writeTimeOut = writeTimeOut;
            return this;
        }

        public Builder setTimeUnit(TimeUnit timeUnit) {
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

        public Configuration build() {
            return new Configuration(this);
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
}
