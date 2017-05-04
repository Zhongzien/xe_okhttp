package com.okhttplib.utils;

import android.text.TextUtils;

import com.okhttplib.interceptor.MediaTypeInterceptor;

/**
 * Created by Administrator on 2017/5/3 0003.
 */

public class DefaultUploadInterceptor implements MediaTypeInterceptor {
    @Override
    public String intercept(String filePath) {

        if (!TextUtils.isEmpty(filePath) && filePath.contains(".")) {
            String suffix = filePath.substring(filePath.indexOf(".") + 1);
            if ("png".equals(suffix)) {
                suffix = "image/png";
            } else if ("jpg".equals(suffix)) {
                suffix = "image/jpg";
            } else if ("jpeg".equals(suffix)) {
                suffix = "image/jpeg";
            } else if ("ioc".equals(suffix)) {
                suffix = "image/ico";
            } else if ("gif".equals(suffix)) {
                suffix = "image/gif";
            } else if ("bmp".equals(suffix)) {
                suffix = "image/bmp";
            } else {
                suffix = "multipart/form-data";
            }
            return suffix;
        }

        return null;
    }
}
