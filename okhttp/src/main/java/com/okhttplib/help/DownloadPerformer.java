package com.okhttplib.help;

import android.text.TextUtils;

import com.okhttplib.HttpInfo;
import com.okhttplib.annotation.DownloadStatus;
import com.okhttplib.bean.DownloadFileInfo;
import com.okhttplib.config.Configuration;
import com.okhttplib.interceptor.DownloadInterceptor;
import com.okhttplib.utils.DigitalSignature;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/9 0009.
 */

public class DownloadPerformer extends BasicOkPerformer {
    String TAG = DownloadPerformer.class.getSimpleName();

    private OkHttpClient.Builder okBuilder;
    private String defaultDirPaht;

    private static ConcurrentHashMap<String, DownloadFileInfo> downloadMap;

    DownloadPerformer(Configuration config) {
        super(config);
        okBuilder = newOkHttpClientBuilder(config);
        defaultDirPaht = config.getSaveDirPath();
    }

    public void doRequestAsync(HttpCommand command) {
        final HttpInfo info = command.getInfo();
        final DownloadFileInfo fileInfo = info.getDownloadFile();

        long completeSize = getCompleteSize(info.getDownloadFile());
        if (downloadMap == null) {
            downloadMap = new ConcurrentHashMap<>();
        }
        if (downloadMap.containsKey(fileInfo.getSaveSignaturFileName())) {
            return;
        }
        downloadMap.put(fileInfo.getSaveSignaturFileName(), fileInfo);

        fileInfo.setCompleteSize(completeSize);
        info.addHead("RANGE", "bytes=" + completeSize + "-");

        info.addUrl(fileInfo.getUrl());
        command.setCallBack(fileInfo.getOnProgressCallBack());

        command.doRequestAsync();
    }

    public HttpInfo downloadInfo(HttpInfo info, Response response, Call call) {
        InputStream input = null;
        BufferedInputStream buffInput = null;
        RandomAccessFile accessFile = null;

        final DownloadFileInfo fileInfo = info.getDownloadFile();
        final String saveDir = fileInfo.getSaveDir();

        try {
            long completedLength = fileInfo.getCompleteSize();
            if (TextUtils.isEmpty(response.header("Content-Range"))) {
                completedLength = 0;
                fileInfo.setCompleteSize(completedLength);
            }

            input = response.body().byteStream();
            buffInput = new BufferedInputStream(input);
            accessFile = new RandomAccessFile(saveDir + fileInfo.getSaveSignaturFileName(), "rwd");
            accessFile.seek(completedLength);

            byte[] buff = new byte[2048];
            int current = 0;
            fileInfo.setDownloadStatus(DownloadStatus.DOWNLOADING);
            while ((current = buffInput.read(buff)) > 0 && DownloadStatus.DOWNLOADING == fileInfo.getDownloadStatus()) {
                accessFile.write(buff, 0, current);
                completedLength += current;
            }

            if (DownloadStatus.PAUSE == fileInfo.getDownloadStatus()) {
                return updateInfo(info, HttpInfo.DOWNLOAD_PAUSE, fileInfo.getSaveFileName());
            }

            if (DownloadStatus.STOP == fileInfo.getDownloadStatus()) {
                File file = new File(saveDir, fileInfo.getSaveSignaturFileName());
                if (file.exists() && file.isFile()) {
                    file.delete();
                }
                return updateInfo(info, HttpInfo.DOWNLOAD_STOP, fileInfo.getSaveFileName());
            }

            if (DownloadStatus.DOWNLOADING == fileInfo.getDownloadStatus()) {
                fileInfo.setDownloadStatus(DownloadStatus.COMPLETE);
                File file = new File(saveDir, fileInfo.getSaveFileNameWithExtension());
                if (file.exists() && file.isFile()) {
                    file = new File(saveDir, fileInfo.getSaveCopeFileNameWithExtension());
                }
                File oldFile = new File(saveDir + fileInfo.getSaveSignaturFileName());

                if (oldFile.exists() && oldFile.isFile()) {
                    oldFile.renameTo(file);
                }
                return updateInfo(info, HttpInfo.NET_SUCCESS, file.getAbsolutePath());
            }

        } catch (Exception e) {
            e.printStackTrace();
            updateInfo(info, HttpInfo.CONNECTION_INTERRUPTION, e.getMessage());
        } finally {
            try {
                if (buffInput != null) buffInput.close();
                if (input != null) input.close();
                if (accessFile != null) accessFile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (downloadMap != null)
                downloadMap.remove(fileInfo.getSaveSignaturFileName());
        }

        return info;
    }

    private long getCompleteSize(DownloadFileInfo info) {
        String url = info.getUrl();
        //获取扩展名
        String extension = "";
        if (url.contains("."))
            extension = url.substring(url.lastIndexOf(".") + 1);

        if (!TextUtils.isEmpty(extension)) {
            String fileName = info.getSaveFileName();
            String stamp = buildStamp();
            if (!fileName.contains(".")) {
                info.setSaveFileNameWithExtension(fileName + "." + extension);
                info.setSaveCopeFileNameWithExtension(fileName + "_" + stamp + "." + extension);
            } else {
                info.setSaveFileNameWithExtension(fileName);

                String lastName = fileName.substring(fileName.lastIndexOf("."));
                info.setSaveCopeFileNameWithExtension(fileName.replace(lastName, "_" + stamp + lastName));
            }
        }

        String simpleDir = TextUtils.isEmpty(info.getSaveDir()) ? defaultDirPaht : info.getSaveDir();
        if (mkdirs(simpleDir)) {
            info.setSaveDir(simpleDir);
        }

        try {
            String signatureFileName = DigitalSignature.signatureMD5(url);
            if (TextUtils.isEmpty(signatureFileName))
                info.setSaveSignaturFileName(url);
            else
                info.setSaveSignaturFileName(signatureFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        File file = new File(simpleDir, info.getSaveSignaturFileName());
        if (file.exists() && file.isFile()) {
            return file.length();
        }

        return 0L;
    }

    private boolean mkdirs(String path) {
        File file = new File(path);
        return file.exists() || file.mkdirs();
    }

    private String buildStamp() {
        int random = (int) (Math.random() * 1000);
        return System.currentTimeMillis() + "_" + random;
    }

    public OkHttpClient buildUpOrDownHttpClient(HttpInfo info) {
        return okBuilder.addInterceptor(new DownloadInterceptor(info.getDownloadFile())).build();
    }

    static void updateDownloadStatus(String key, @DownloadStatus int status) {
        try {
            if (TextUtils.isEmpty(key) || downloadMap == null) return;
            DownloadFileInfo info = downloadMap.get(DigitalSignature.signatureMD5(key));
            if (info != null) {
                info.setDownloadStatus(status);
            }
        } catch (Exception e) {

        }
    }

}
