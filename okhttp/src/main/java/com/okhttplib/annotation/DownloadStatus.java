package com.okhttplib.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Administrator on 2017/5/18 0018.
 */
@IntDef({DownloadStatus.INIT, DownloadStatus.STOP, DownloadStatus.PAUSE, DownloadStatus.COMPLETE, DownloadStatus.DOWNLOADING})
@Retention(RetentionPolicy.SOURCE)
public @interface DownloadStatus {

    int INIT = 0x0FF;//初始化
    int STOP = 0x1FF;//停止
    int PAUSE = 0x2FF;//暂停
    int COMPLETE = 0x3FF;//完成
    int DOWNLOADING = 0x4FF;

}
