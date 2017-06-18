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

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class NetWorkActivity extends AppCompatActivity {
    private static final String TAG = NetWorkActivity.class.getSimpleName();

    private final static String url_head = "";
    private final static String url_get = url_head + "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_network);
    }

    public void doPostAsync(View v) {
        OkHttpInvoker.getBuilder().setUrl(url_head)
                .setCallTag(TAG)
                .addParam("", "")
                .build().doPostAsync(new OnResultCallBack() {
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
        OkHttpInvoker.getBuilder().setUrl(url_get)
                .setCallTag(TAG)
                .build().doGetAsync(new OnResultCallBack() {
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
                OkHttpInvoker.getBuilder().setUrl(url_head)
                        .setCallTag(TAG)
                        .addParam("", "")
                        .build().doPostSync(new OnResultCallBack() {
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
                OkHttpInvoker.getBuilder().setUrl(url_get)
                        .setCallTag(TAG)
                        .build().
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
