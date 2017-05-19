package ui.xe.com.xe_okhttp;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpInvoker;
import com.okhttplib.callback.OnProgressCallBack;

/**
 * Created by Administrator on 2017/5/9 0009.
 */

public class DownLoadActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = DownLoadActivity.class.getSimpleName();

    private String url_ = "https://sm.wdjcdn.com/release/files/jupiter/5.52.20.13520/wandoujia-web_seo_baidu_homepage.apk";

    private String url = "http://xiuxiu.android.dl.meitu.com/xiuxiu.apk";
    String downloadFileDir = Environment.getExternalStorageDirectory().getPath() + "/okHttp_download/";

    private TextView tvPercent;
    private ProgressBar pBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_download_file);

        findViewById(R.id.download_btn).setOnClickListener(this);
        findViewById(R.id.pause_btn).setOnClickListener(this);
        findViewById(R.id.stop_btn).setOnClickListener(this);

        tvPercent = (TextView) findViewById(R.id.percent_tv);
        pBar = (ProgressBar) findViewById(R.id.uploadProgressOne);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download_btn:
                OkHttpInvoker.getBuilder().
                        addDownloadFile(url, downloadFileDir, "download_test.apk", new OnProgressCallBack() {
                            @Override
                            public void onProgress(long percent, long curLength, long totalLength) {
                                Log.i(TAG, "curLength:" + curLength);

//                                tvPercent.setText(percent + "%");
//                                pBar.setProgress((int) curLength);
                            }

                            @Override
                            public void onResponse(HttpInfo info) {
                                Log.i(TAG, "onResponse:" + info.getResultBody());
                            }
                        }).
                        build().doDownloadAsync();
                break;
            case R.id.pause_btn:
                OkHttpInvoker.pause(url);
                break;
            case R.id.stop_btn:
                OkHttpInvoker.stop(url);
                break;
        }
    }

}