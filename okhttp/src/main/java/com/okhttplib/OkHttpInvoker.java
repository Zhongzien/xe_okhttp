package com.okhttplib;

import android.text.TextUtils;

import com.okhttplib.annotation.DownloadStatus;
import com.okhttplib.annotation.RequestMethod;
import com.okhttplib.bean.DownloadFileInfo;
import com.okhttplib.bean.UploadFileInfo;
import com.okhttplib.callback.OnProgressCallBack;
import com.okhttplib.config.Configuration;
import com.okhttplib.callback.OnResultCallBack;
import com.okhttplib.help.HttpCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 命令请求者角色
 */

public class OkHttpInvoker implements OkHttpInter {

    private static Configuration mConfig;

    private Builder mBuilder;

    private OkHttpInvoker(Builder builder) {
        mBuilder = builder;
    }

    @Override
    public void doPostAsync(final OnResultCallBack callBack) {
        HttpCommand.getBuilder().
                setConfiguration(buildConfig()).
                setInvokerBuilder(buildHttpInfo()).
                setOnResultCallBack(callBack).
                setRequestMethod(RequestMethod.POST).
                build().doRequestAsync();
    }

    @Override
    public void doGetAsync(final OnResultCallBack callBack) {
        HttpCommand.getBuilder().
                setConfiguration(buildConfig()).
                setInvokerBuilder(buildHttpInfo()).
                setOnResultCallBack(callBack).
                setRequestMethod(RequestMethod.GET).
                build().doRequestAsync();
    }

    @Override
    public void doPostSync(OnResultCallBack callBack) {
        HttpCommand.getBuilder().
                setConfiguration(buildConfig()).
                setInvokerBuilder(buildHttpInfo()).
                setOnResultCallBack(callBack).
                setRequestMethod(RequestMethod.POST).
                build().doRequestSync();
    }

    @Override
    public void doGetSync(OnResultCallBack callBack) {
        HttpCommand.getBuilder().
                setConfiguration(buildConfig()).
                setInvokerBuilder(buildHttpInfo()).
                setOnResultCallBack(callBack).
                setRequestMethod(RequestMethod.GET).
                build().doRequestSync();
    }

    @Override
    public void doUploadAsync(OnResultCallBack callBack) {
        HttpCommand.getBuilder().
                setConfiguration(buildConfig()).
                setInvokerBuilder(buildHttpInfo().
                        addUploadFiles(mBuilder.uploadFiles)).
                setOnResultCallBack(callBack).
                build().doFileUploadAsync();
    }

    @Override
    public void doDownloadAsync() {
        List<DownloadFileInfo> downloadFiles = mBuilder.downloadFiles;
        if (downloadFiles == null)
            throw new NullPointerException("the download files list can not null!");
        for (DownloadFileInfo info : downloadFiles) {
            HttpCommand.getBuilder().
                    setConfiguration(buildConfig()).
                    setInvokerBuilder(buildHttpInfo().
                            addDownloadFile(info)).
                    build().doDownloadAsync();
        }
    }

    public static void config(Configuration config) {
        if (mConfig == null) {
            synchronized (OkHttpInvoker.class) {
                if (mConfig == null)
                    mConfig = config;
            }
        }
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    private HttpInfo buildHttpInfo() {
        HttpInfo info = HttpInfo.getInstance().
                addUrl(mBuilder.url).
                addParams(mBuilder.params).
                addHeads(mBuilder.heads);
        return info;
    }

    public static class Builder {

        ////普通请求
        private String url;
        private HashMap<String, String> params;
        private HashMap<String, String> heads;
        //文件上传
        List<UploadFileInfo> uploadFiles;
        //文件下载
        private List<DownloadFileInfo> downloadFiles;

        public Builder() {
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder addParams(HashMap<String, String> params) {
            if (params != null) {
                if (this.params == null) this.params = new HashMap<>();
                this.params.putAll(params);
            }
            return this;
        }

        public Builder addParam(String key, String value) {
            if (!TextUtils.isEmpty(key)) {
                if (params == null) params = new HashMap<>();
                params.put(key, value);
            }
            return this;
        }

        public Builder addHeads(HashMap<String, String> heads) {
            if (heads != null) {
                if (this.heads == null) this.heads = new HashMap<>();
                this.heads.putAll(heads);
            }
            return this;
        }

        public Builder addHead(String key, String value) {
            if (!TextUtils.isEmpty(key)) {
                if (heads == null) heads = new HashMap<>();
                heads.put(key, value);
            }
            return this;
        }

        public Builder addUploadFiles(List<UploadFileInfo> files) {
            if (files != null) {
                if (uploadFiles == null) uploadFiles = new ArrayList<>();
                uploadFiles.addAll(files);
            }
            return this;
        }

        public Builder addUploadFile(String uploadFormat, String fileAbsolutePath) {
            if (!TextUtils.isEmpty(fileAbsolutePath)) {
                if (uploadFiles == null) uploadFiles = new ArrayList<>();
                uploadFiles.add(new UploadFileInfo(uploadFormat, fileAbsolutePath));
            }
            return this;
        }

        public Builder addDownloadFiles(List<DownloadFileInfo> files) {
            if (files != null) {
                if (downloadFiles == null) downloadFiles = new ArrayList<>();
                downloadFiles.addAll(files);
            }
            return this;
        }

        public Builder addDownloadFile(String url, String saveDir, String saveFileName) {
            addDownloadFile(url, saveDir, saveFileName, null);
            return this;
        }

        public Builder addDownloadFile(String url, String saveDir, String saveFileName, OnProgressCallBack callBack) {
            if (!TextUtils.isEmpty(url)) {
                if (downloadFiles == null) downloadFiles = new ArrayList<>();
                downloadFiles.add(new DownloadFileInfo(url, saveDir, saveFileName, callBack));
            }
            return this;
        }

        public OkHttpInter build() {
            return new OkHttpInvoker(this);
        }

    }

    private Configuration buildConfig() {
        if (mConfig == null) {
            synchronized (OkHttpInvoker.class) {
                if (mConfig == null)
                    new Configuration.Builder().bindConfig();
            }
        }
        return mConfig;
    }

    public static void stop(String key) {
        HttpCommand.updateDownloadStatus(key, DownloadStatus.STOP);
    }

    public static void pause(String key) {
        HttpCommand.updateDownloadStatus(key, DownloadStatus.PAUSE);
    }
}
