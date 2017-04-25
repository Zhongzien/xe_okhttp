package ui.xe.com.xe_okhttp.base;

import android.util.Log;

import com.okhttplib.interceptor.ParamsInterceptor;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class MyInterceptor implements ParamsInterceptor {
    @Override
    public HashMap<String, String> intercept(HashMap<String, String> params) {
        Log.i("MyInterceptor", "" + params);
        return params;
    }
}
