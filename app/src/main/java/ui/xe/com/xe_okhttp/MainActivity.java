package ui.xe.com.xe_okhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.okhttplib.OkHttpInvoker;
import com.okhttplib.callback.OnResultCallBack;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, Object> map = new HashMap<>();
        map.put("app", "life.time");
        map.put("appkey", "10003");
        map.put("sign", "b59bc3ef6191eb9f747dd4e83c99f2a4");
        map.put("format", "json");

        OkHttpInvoker.getDefaultBuilder().setUrl("http://api.k780.com:88/").addParams(map).build().doPostAsync(new OnResultCallBack() {
            @Override
            public void onSuccess(String result, String msg) {
                Log.i(TAG, "doPostAsync:" + result);
            }

            @Override
            public void onFailure(String msg) {
                Log.i(TAG, "doPostAsync:" + msg);
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
            public void onSuccess(String result, String msg) {
                Log.i(TAG, "doGetAsync:" + result);
            }

            @Override
            public void onFailure(String msg) {
                Log.i(TAG, "doGetAsync:" + msg);
            }
        });

    }
}
