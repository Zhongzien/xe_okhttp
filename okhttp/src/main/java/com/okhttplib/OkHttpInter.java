package com.okhttplib;

import android.support.annotation.NonNull;

import com.okhttplib.annotation.DownloadStatus;
import com.okhttplib.bean.DownloadFileInfo;
import com.okhttplib.bean.UploadFileInfo;
import com.okhttplib.callback.OnProgressCallBack;
import com.okhttplib.callback.OnResultCallBack;
import com.okhttplib.help.HttpCommand;
import com.okhttplib.manage.BasicCallManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public abstract class OkHttpInter {

    /**
     *
     * @param key
     * @param call
     */
    public static void putCall(@NonNull String key, Call call) {
        BasicCallManage.putCall(key, call);
    }

    /**
     *
     * @param key
     */
    public static void removeCallOrSet(@NonNull String key) {
        BasicCallManage.removeCallOrSet(key, null);
    }

    /**
     *
     * @param key
     * @param call
     */
    public static void removeCallOrSer(@NonNull String key, Call call) {
        BasicCallManage.removeCallOrSet(key, call);
    }

    /**
     *
     * @param key
     * @param call
     */
    public static void removeCall(@NonNull String key, Call call) {
        BasicCallManage.removeCall(key, call);
    }

    /**
     *
     * @param key
     */
    public static void stop(@NonNull String key) {
        HttpCommand.updateDownloadStatus(key, DownloadStatus.STOP);
    }

    /**
     *
     * @param key
     */
    public static void pause(@NonNull String key) {
        HttpCommand.updateDownloadStatus(key, DownloadStatus.PAUSE);
    }

    /**
     * 异步post请求
     */
    public abstract void doPostAsync(OnResultCallBack callBack);

    /**
     * 异步get请求
     * @param callBack
     */
    public abstract void doGetAsync(OnResultCallBack callBack);

    /**
     * 同步Post请求
     *
     * @return
     */
    public abstract void doPostSync(OnResultCallBack callBack);

    /**
     * 同步Get请求
     *
     * @return
     */
    public abstract void doGetSync(OnResultCallBack callBack);

    /**
     * 异步上传文件
     *
     * @return
     */
    public abstract void doUploadAsync(OnResultCallBack callBack);

    public abstract void doDownloadAsync();

    public interface BuilderInter {

        public BuilderInter setCallTag(@NonNull String tag);

        public BuilderInter setUrl(String url);

        public BuilderInter addParams(HashMap<String, String> params);

        public BuilderInter addParam(String key, String value);

        public BuilderInter addHeads(HashMap<String, String> heads);

        public BuilderInter addHead(String key, String value);

        public BuilderInter addUploadFiles(List<UploadFileInfo> files);

        public BuilderInter addUploadFile(String uploadFormat, String fileAbsolutePath);

        public BuilderInter addDownloadFiles(List<DownloadFileInfo> files);

        public BuilderInter addDownloadFile(String url, String saveFileName, OnProgressCallBack callBack);

        public BuilderInter addDownloadFile(String url, String saveDir, String saveFileName, OnProgressCallBack callBack);

        public OkHttpInter build();

    }
}
