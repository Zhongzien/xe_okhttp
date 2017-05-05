package ui.xe.com.xe_okhttp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpInvoker;
import com.okhttplib.callback.OnResultCallBack;

import ui.xe.com.xe_okhttp.util.FilePathUtil;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = UploadActivity.class.getSimpleName();
//    private String url = "http://192.168.120.206:8080/office/upload/uploadFile";

    private String url = "http://192.168.1.128:8091/upload/uploadSingleImage";

    private String filePathOne;
    private String filePathTwo;

    private TextView tvOne;
    private TextView tvTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_upload_file);

        findViewById(R.id.chooseImgBtnOne).setOnClickListener(this);
        findViewById(R.id.chooseImgBtnTwo).setOnClickListener(this);
        findViewById(R.id.uploadImgBtnOne).setOnClickListener(this);
        findViewById(R.id.uploadImgBtnTwo).setOnClickListener(this);
        findViewById(R.id.uploadImgBtnMulti).setOnClickListener(this);

        tvOne = (TextView) findViewById(R.id.ivImageOne);
        tvTwo = (TextView) findViewById(R.id.ivImageTwo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chooseImgBtnOne:
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
                break;
            case R.id.chooseImgBtnTwo:
                Intent intentTwo = new Intent();
                intentTwo.setType("*/*");
                intentTwo.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intentTwo, 2);
                break;
            case R.id.uploadImgBtnOne:
                if (!TextUtils.isEmpty(filePathOne)) {
                    uploadImgOne();
                } else {
                    Toast.makeText(this, "请先选择上传的图片！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.uploadImgBtnTwo:
                if (!TextUtils.isEmpty(filePathTwo)) {
                    uploadImgTwo();
                } else {
                    Toast.makeText(this, "请先选择上传的图片！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.uploadImgBtnMulti:
                if (!TextUtils.isEmpty(filePathOne) && !TextUtils.isEmpty(filePathTwo)) {
                    uploadImgMulti();
                } else {
                    Toast.makeText(this, "请先选择两张图片！", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void uploadImgTwo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpInvoker.getDefaultBuilder().
                        addUploadFile("file2", filePathTwo).
                        setUrl(url).build().
                        doUploadSync(new OnResultCallBack() {
                    @Override
                    public void onResponse(HttpInfo info) {
                        if (info.isSuccess()) {
                            Log.i(TAG, "success:" + info.getResultBody());
                        } else {
                            Log.i(TAG, "failure:" + info.getResultBody());
                        }
                    }
                });
            }
        }).start();
    }

    private void uploadImgMulti() {
        OkHttpInvoker.getDefaultBuilder().
                setUrl(url).addUploadFile("file1", filePathOne).
                addUploadFile("file2", filePathTwo).
                build().doUploadAsync(new OnResultCallBack() {
            @Override
            public void onResponse(HttpInfo info) {
                if (info.isSuccess()) {
                    Log.i(TAG, "success:" + info.getResultBody());
                } else {
                    Log.i(TAG, "failure:" + info.getResultBody());
                }
            }
        });
    }

    private void uploadImgOne() {
        OkHttpInvoker.getDefaultBuilder().
                setUrl(url).addUploadFile("file", filePathOne).
                build().doUploadAsync(new OnResultCallBack() {
            @Override
            public void onResponse(HttpInfo info) {
                if (info.isSuccess()) {
                    Log.i(TAG, "success:" + info.getResultBody());
                } else {
                    Log.i(TAG, "failure:" + info.getResultBody());
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri contentUri = data.getData();
            String path = FilePathUtil.getFilePathFromUri(this, contentUri);
            Log.i(TAG, "path:" + path);
            if (TextUtils.isEmpty(path)) {
                Toast.makeText(this, "获取图片地址失败", Toast.LENGTH_LONG).show();
                return;
            }
            if (requestCode == 1) {
                filePathOne = path;
                tvOne.setText(filePathOne);
            }
            if (requestCode == 2) {
                filePathTwo = path;
                tvTwo.setText(filePathTwo);
            }
        }
    }

}