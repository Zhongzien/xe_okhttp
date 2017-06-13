package com.okhttplib.manage.cookie;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.okhttplib.utils.DigitalSignature;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by Administrator on 2017/6/10 0010.
 */

public class PersistentCookieController {

    private final static String COOKIES_PERSISTENT_SHARE_KEY = "cookies_persistent_share_id";

    private final Map<String, ConcurrentHashMap<String, Cookie>> cookies;
    private SharedPreferences preferences;

    public PersistentCookieController(Context context, boolean isCookiesStrong) {
        cookies = new HashMap<>();

        if (isCookiesStrong) {
            preferences = context.getSharedPreferences(COOKIES_PERSISTENT_SHARE_KEY, context.MODE_PRIVATE);
            Map<String, ?> map = preferences.getAll();
            for (Map.Entry<String, ?> entry : map.entrySet()) {
                String[] tokens = TextUtils.split(",", String.valueOf(entry.getValue()));
                for (String token : tokens) {
                    String encode = preferences.getString(token, null);
                    if (!TextUtils.isEmpty(encode)) {
                        Cookie cookie = decode(encode);
                        if (cookie != null) {
                            if (!cookies.containsKey(entry.getKey())) {
                                cookies.put(entry.getKey(), new ConcurrentHashMap<String, Cookie>());
                            }
                            cookies.get(entry.getKey()).put(token, cookie);
                        }
                    }
                }
            }
        }
    }

    public String getToken(Cookie cookie) {
        return cookie.name() + "@" + cookie.value();
    }

    public void add(HttpUrl url, Cookie cookie) {
        String token = getToken(cookie);

        ConcurrentHashMap<String, Cookie> curMap = cookies.get(url.host());
        //if the cookie is persistent put refresh the data of the ConcurrentHashMap,
        //else remove the invalid cookie.
        if (cookie.persistent()) {
            if (curMap == null) {
                curMap = new ConcurrentHashMap<>();
                cookies.put(url.host(), curMap);
            }

            curMap.put(token, cookie);
            //Put the cookie to persistent
            if (preferences != null) {
                preferences.edit().putString(url.host(), TextUtils.join(",", curMap.keySet()))
                        .putString(token, encode(new CookieSerializable(cookie))).apply();
            }
        } else {
            if (curMap != null) {
                curMap.remove(token);
                //Remove the cookie of lose efficacy
                if (preferences != null) {
                    preferences.edit().remove(token)
                            .putString(url.host(), TextUtils.join(",", curMap.keySet())).apply();
                }
            }
        }
    }

    public List<Cookie> get(HttpUrl url) {
        List<Cookie> list = new ArrayList<>();
        if (cookies.containsKey(url.host()))
            list.addAll(cookies.get(url.host()).values());
        return list;
    }

    public void remove(HttpUrl url, Cookie cookie) {
        String token = getToken(cookie);
        if (cookies.containsKey(url.host()) && cookies.get(url.host()).containsKey(token)) {
            cookies.get(url.host()).remove(token);
            if (preferences != null) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(url.host(), TextUtils.join(",", cookies.get(url.host()).keySet()));
                editor.remove(token).apply();
            }
        }
    }

    public void removeAll() {
        cookies.clear();
        if (preferences != null)
            preferences.edit().clear().commit();
    }

    public String encode(CookieSerializable serializable) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
            objOut.writeObject(serializable);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return DigitalSignature.byteArrayToHexString(byteOut.toByteArray());
    }

    public Cookie decode(String cookieString) {
        Cookie cookie = null;
        try {
            byte[] bytes = DigitalSignature.hexStringToByteArray(cookieString);
            ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
            ObjectInputStream objInput = new ObjectInputStream(byteInput);
            CookieSerializable serializable = (CookieSerializable) objInput.readObject();
            cookie = serializable.getCookie();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return cookie;
    }

}
