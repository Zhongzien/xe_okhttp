package ui.xe.com.xe_okhttp;

import android.content.Intent;
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
        startActivity(new Intent(this, NetWorkActivity.class));
    }

    public void doGetAsync(View v) {
        startActivity(new Intent(this, UploadActivity.class));
    }

    public void doPostSync(View v) {
        startActivity(new Intent(this, DownLoadActivity.class));
    }

    public void doGetSync(View v) {

    }

}
