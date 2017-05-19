package com.okhttplib.help;

import android.text.TextUtils;

import com.okhttplib.HttpInfo;
import com.okhttplib.bean.UploadFileInfo;
import com.okhttplib.config.Configuration;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 文件上传这行者
 * 负责文件上传
 * 分别提供同步和异步请求
 */

public class UploadPerformer extends BasicOkPerformer  {

    UploadPerformer(Configuration config) {
        super(config);
    }

    public void doRequestAsync(HttpCommand command) {
        command.doRequestAsync();
    }

    @Deprecated
    public void doRequestSync(HttpCommand command) {
        command.doRequestSync();
    }

    private Request buildRequest(final HttpInfo info) {
        MultipartBody.Builder mbBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        //配置上传的附属信息
        HashMap<String, String> params = info.getParams();
        if (paramsInterceptor != null) {
            //使用clone技术(原型模式)目的是防止原始数据被篡改
            params = paramsInterceptor.intercept(params == null ? null : (HashMap<String, String>) params.clone());
        }
        if (params != null && !params.isEmpty()) {
            for (String key : info.getParams().keySet()) {
                String value = info.getParams().get(key);
                value = value == null ? "" : value;
                mbBuilder.addFormDataPart(key, value);
            }
        }

        //配置上传文件(需要上传的实体)
        if (info.getUploadFiles() != null) {
            for (UploadFileInfo fileInfo : info.getUploadFiles()) {
                String filePath = fileInfo.getFileAbsolutePath();
                File file = new File(filePath);

                if (TextUtils.isEmpty(filePath))
                    throw new IllegalArgumentException("file path is illegal argument");

                mbBuilder.addFormDataPart(fileInfo.getUploadFormat(),
                        file.getName(),
                        RequestBody.create(MediaType.parse(mediaTypeInterceptor.intercept(filePath)), file));
            }
        }

        Request.Builder builder = new Request.Builder();
        builder.url(info.getUrl()).post(mbBuilder.build());
        //配置http head
        setRequestHeads(info, builder);
        return builder.build();
    }

}
