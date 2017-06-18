package ui.xe.com.xe_okhttp.app;

import android.app.Application;

import com.okhttplib.Configuration;

import ui.xe.com.xe_okhttp.base.MyInterceptor;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Configuration.getConfigBuilder(this).addParamsInterceptor(new MyInterceptor()).bindConfig();
    }
}
