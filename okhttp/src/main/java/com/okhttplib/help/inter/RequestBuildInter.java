package com.okhttplib.help.inter;

import com.okhttplib.HttpInfo;

import okhttp3.Request;

/**
 * Created by Administrator on 2017/5/3 0003.
 */

public interface RequestBuildInter {

    Request buildFileRequest(HttpInfo info);

}
