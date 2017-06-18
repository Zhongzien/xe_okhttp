package com.okhttplib.manage.cookie;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import okhttp3.Cookie;

/**
 * Created by Administrator on 2017/6/10 0010.
 */

public class CookieSerializable implements Serializable {
    public final transient Cookie cookie;

    private transient Cookie decodeCookie;

    public CookieSerializable(Cookie cookie) {
        this.cookie = cookie;
    }

    public Cookie getCookie() {
        Cookie result = cookie;
        if (result == null) {
            result = decodeCookie;
        }
        return result;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(cookie.name());
        out.writeObject(cookie.value());
        out.writeLong(cookie.expiresAt());
        out.writeObject(cookie.domain());
        out.writeObject(cookie.path());
        out.writeBoolean(cookie.secure());
        out.writeBoolean(cookie.httpOnly());
        out.writeBoolean(cookie.persistent());
        out.writeBoolean(cookie.hostOnly());
    }

    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
        String name = (String) input.readObject();
        String value = (String) input.readObject();
        long expiresAt = input.readLong();
        String domain = (String) input.readObject();
        String path = (String) input.readObject();
        boolean secure = input.readBoolean();
        boolean httpOnly = input.readBoolean();
        boolean persistent = input.readBoolean();
        boolean hostOnly = input.readBoolean();

        Cookie.Builder builder = new Cookie.Builder()
                .name(name).value(value)
                .expiresAt(expiresAt).path(path);

        builder = hostOnly ? builder.hostOnlyDomain(domain) : builder.domain(domain);
        builder = secure ? builder.secure() : builder;
        builder = httpOnly ? builder.httpOnly() : builder;

        decodeCookie = builder.build();
    }

}
