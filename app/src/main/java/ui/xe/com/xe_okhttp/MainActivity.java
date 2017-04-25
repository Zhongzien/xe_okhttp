package ui.xe.com.xe_okhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpInvoker;
import com.okhttplib.callback.OnResultCallBack;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private String url = "http://api.k780.com:88/?app=life.time&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
    }

    public void doPostAsync(View v) {
        HashMap<String, String> map = new HashMap<>();
        map.put("app", "life.time");
        map.put("appkey", "10003");
        map.put("sign", "b59bc3ef6191eb9f747dd4e83c99f2a4");
        map.put("format", "json");

        OkHttpInvoker.getDefaultBuilder().setUrl("http://api.k780.com:88/").addParams(map).build().doPostAsync(new OnResultCallBack() {
            @Override
            public void onResponse(HttpInfo info) {
                if (info.isSuccess()) {
                    Log.i(TAG, "doPostAsync:" + info.getResultBody());
                } else {
                    Log.i(TAG, "doPostAsync:" + info.getMsg());
                }
            }
        });
    }

    public void doGetAsync(View v) {
        OkHttpInvoker.getDefaultBuilder().setUrl("http://api.k780.com:88/").
                addParam("app", "life.time").
                addParam("appkey", "10003").
                addParam("sign", "b59bc3ef6191eb9f747dd4e83c99f2a4").
                addParam("format", "json").build().doGetAsync(new OnResultCallBack() {

            @Override
            public void onResponse(HttpInfo info) {
                if (info.isSuccess()) {
                    Log.i(TAG, "doGetAsync:" + info.getResultBody());
                } else {
                    Log.i(TAG, "doGetAsync:" + info.getMsg());
                }
            }
        });
    }

    public void doPostSync(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpInvoker.getDefaultBuilder().setUrl("http://api.k780.com:88/").
                        addParam("app", "life.time").
                        addParam("appkey", "10003").
                        addParam("sign", "b59bc3ef6191eb9f747dd4e83c99f2a4").
                        addParam("format", "json").build().
                        doPostSync(new OnResultCallBack() {
                            @Override
                            public void onResponse(HttpInfo info) {
                                if (info.isSuccess()) {
                                    Log.i(TAG, "doPostSync:" + info.getResultBody());
                                } else {
                                    Log.i(TAG, "doPostSync:" + info.getMsg());
                                }
                            }
                        });
            }
        }).start();
    }

    public void doGetSync(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpInvoker.getDefaultBuilder().setUrl(url).build().
                        doGetSync(new OnResultCallBack() {
                            @Override
                            public void onResponse(HttpInfo info) {
                                if (info.isSuccess()) {
                                    Log.i(TAG, "doGetSync:" + info.getResultBody());
                                } else {
                                    Log.i(TAG, "doGetAsync:" + info.getMsg());
                                }
                            }
                        });
            }
        }).start();
    }

}
