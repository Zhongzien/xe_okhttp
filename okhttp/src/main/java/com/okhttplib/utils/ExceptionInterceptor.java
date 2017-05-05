package com.okhttplib.utils;

import com.okhttplib.HttpInfo;
import com.okhttplib.interceptor.MsgInterceptor;

/**
 * Created by Administrator on 2017/4/21 0021.
 */

public class ExceptionInterceptor implements MsgInterceptor {

    @Override
    public void intercept(HttpInfo info) {
        if (info.getLocalCode() == HttpInfo.CLIENT_4XX || info.getLocalCode() == HttpInfo.SERVICE_5XX) {
            int netCode = info.getNetCode();
            String msg = info.getMsg();
            switch (netCode) {
                case 400:
                    msg = "请求出现语法错误!";
                    break;
                case 401:
                    msg = "访问被拒绝!";
                    break;
                case 403:
                    msg = "资源不可用!";
                    break;
                case 404:
                    msg = "无法找到指定位置的资源!";
                    break;
                case 500:
                    msg = "服务器内部错误!";
                    break;
                case 502:
                    msg = "错误网关!";
                    break;
                case 503:
                    msg = "服务不可用!";
                    break;
                case 504:
                    msg = "网关超时!";
                    break;
            }
            info.setMsg(netCode + ":" + msg);
        }

    }

}
