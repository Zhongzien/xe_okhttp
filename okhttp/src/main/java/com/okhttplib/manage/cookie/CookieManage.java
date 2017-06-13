package com.okhttplib.manage.cookie;

import com.okhttplib.Configuration;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by Administrator on 2017/6/3 0003.
 */

public final class CookieManage implements CookieJar {
    private PersistentCookieController controller;

    public CookieManage(Configuration config) {
        controller = new PersistentCookieController(config.getContext(), config.isCookieStrong());
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && !cookies.isEmpty()) {
            for (Cookie cookie : cookies)
                controller.add(url, cookie);
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return controller.get(url);
    }

}
