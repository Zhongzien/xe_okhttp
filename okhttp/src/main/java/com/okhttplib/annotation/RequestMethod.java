package com.okhttplib.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 请求方式
 */
@IntDef({RequestMethod.POST, RequestMethod.GET, RequestMethod.FORM})
@Retention(RetentionPolicy.SOURCE)
public @interface RequestMethod {
    int POST = 0x0F;
    int GET = 0x1F;
    int FORM = 0x2F;
}
