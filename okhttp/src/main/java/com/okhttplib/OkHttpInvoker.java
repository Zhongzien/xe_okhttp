package com.okhttplib;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.okhttplib.annotation.DownloadStatus;
import com.okhttplib.annotation.RequestMethod;
import com.okhttplib.bean.DownloadFileInfo;
import com.okhttplib.bean.UploadFileInfo;
import com.okhttplib.callback.OnProgressCallBack;
import com.okhttplib.callback.OnResultCallBack;
import com.okhttplib.help.HttpCommand;
import com.okhttplib.manage.BasicCallManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * 命令请求者角色
 */

public final class OkHttpInvoker extends OkHttpInter {

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
                setRequestMethod(RequestMethod.FORM).
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

    static void config(Configuration config) {
        if (mConfig == null) {
            synchronized (OkHttpInvoker.class) {
                if (mConfig == null)
                    mConfig = config;
            }
        }
    }

    public static BuilderInter getBuilder() {
        return new Builder();
    }

    private HttpInfo buildHttpInfo() {
        HttpInfo info = HttpInfo.getInstance().
                addUrl(mBuilder.url).
                addParams(mBuilder.params).
                addCallTag(mBuilder.callTag).
                addHeads(mBuilder.heads);
        return info;
    }

    public static class Builder implements BuilderInter {

        ////普通请求
        private String url;
        private HashMap<String, String> params;
        private HashMap<String, String> heads;
        //文件上传
        List<UploadFileInfo> uploadFiles;
        //文件下载
        private List<DownloadFileInfo> downloadFiles;
        public String callTag;

        protected Builder() {
        }

        @Override
        public BuilderInter setCallTag(@NonNull String tag) {
            callTag = tag;
            return this;
        }

        @Override
        public BuilderInter setUrl(String url) {
            this.url = url;
            return this;
        }

        @Override
        public BuilderInter addParams(HashMap<String, String> params) {
            if (params != null) {
                if (this.params == null) this.params = new HashMap<>();
                this.params.putAll(params);
            }
            return this;
        }

        @Override
        public BuilderInter addParam(String key, String value) {
            if (!TextUtils.isEmpty(key)) {
                if (params == null) params = new HashMap<>();
                params.put(key, value);
            }
            return this;
        }

        @Override
        public BuilderInter addHeads(HashMap<String, String> heads) {
            if (heads != null) {
                if (this.heads == null) this.heads = new HashMap<>();
                this.heads.putAll(heads);
            }
            return this;
        }

        @Override
        public BuilderInter addHead(String key, String value) {
            if (!TextUtils.isEmpty(key)) {
                if (heads == null) heads = new HashMap<>();
                heads.put(key, value);
            }
            return this;
        }

        @Override
        public BuilderInter addUploadFiles(List<UploadFileInfo> files) {
            if (files != null) {
                if (uploadFiles == null) uploadFiles = new ArrayList<>();
                uploadFiles.addAll(files);
            }
            return this;
        }

        @Override
        public BuilderInter addUploadFile(String uploadFormat, String fileAbsolutePath) {
            if (!TextUtils.isEmpty(fileAbsolutePath)) {
                if (uploadFiles == null) uploadFiles = new ArrayList<>();
                uploadFiles.add(new UploadFileInfo(uploadFormat, fileAbsolutePath));
            }
            return this;
        }

        @Override
        public BuilderInter addDownloadFiles(List<DownloadFileInfo> files) {
            if (files != null) {
                if (downloadFiles == null) downloadFiles = new ArrayList<>();
                downloadFiles.addAll(files);
            }
            return this;
        }

        @Override
        public BuilderInter addDownloadFile(String url, String saveFileName, OnProgressCallBack callBack) {
            addDownloadFile(url, null, saveFileName, callBack);
            return this;
        }

        @Override
        public BuilderInter addDownloadFile(String url, String saveDir, String saveFileName, OnProgressCallBack callBack) {
            if (!TextUtils.isEmpty(url)) {
                if (downloadFiles == null) downloadFiles = new ArrayList<>();
                downloadFiles.add(new DownloadFileInfo(url, saveDir, saveFileName, callBack));
            }
            return this;
        }

        @Override
        public OkHttpInter build() {
            return new OkHttpInvoker(this);
        }

    }

    private Configuration buildConfig() {
        if (mConfig == null) {
            synchronized (OkHttpInvoker.class) {
                if (mConfig == null)
                    Configuration.getDefBuilder().bindConfig();
            }
        }
        return mConfig;
    }

}
