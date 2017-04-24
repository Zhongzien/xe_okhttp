package com.okhttplib.bean;

import android.os.Message;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public abstract class BaseOkMessage implements Serializable {

    public int what;

    public Message build() {
        Message msg = new Message();
        msg.what = what;
        msg.obj = this;
        return msg;
    }

}
