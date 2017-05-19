package com.okhttplib.progress;

import android.os.Message;

import com.okhttplib.bean.DownloadFileInfo;
import com.okhttplib.bean.ProgressMessage;
import com.okhttplib.handler.HttpMainHandler;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by Administrator on 2017/5/16 0016.
 */

public class ProgressResponse extends ResponseBody {

    private ResponseBody originalBody;
    private DownloadFileInfo fileInfo;
    private BufferedSource bufferedSource;

    public ProgressResponse(ResponseBody body, DownloadFileInfo info) {
        originalBody = body;
        fileInfo = info;
    }

    @Override
    public MediaType contentType() {
        return originalBody.contentType();
    }

    /**
     * 當前需要下載的大小
     *
     * @return
     */
    @Override
    public long contentLength() {
        return originalBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(buildSource(originalBody.source()));
        }
        return bufferedSource;
    }

    private Source buildSource(Source source) {
        return new ForwardingSource(source) {
            /** 目前下载总长度*/
            private long curBytes;
            /**下载项目总长度*/
            private long contentBytes;
            /**
             * 05-19 10:18:54.835 21901-21901/ui.xe.com.xe_okhttp I/DownLoadActivity: curLength:54303137
             * 05-19 10:18:54.841 21901-21901/ui.xe.com.xe_okhttp I/DownLoadActivity: curLength:54303137
             * 由于会再执行多一次，所以需要满足 mPercent != percent 才可以继续往下执行
             */
            private long mPercent;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);

                if (curBytes == 0) {
                    //断点下载从原来下载的最后节点处开始下载
                    curBytes = fileInfo.getCompleteSize();
                }
                if (contentBytes == 0) {
                    //总长度 = 当前需要下载的长度 + 过去已经完成的长度
                    contentBytes = contentLength() + curBytes;
                }

                curBytes += bytesRead != -1 ? bytesRead : 0;

                if (fileInfo.getOnProgressCallBack() != null) {
                    long percent = (curBytes * 100) / contentBytes;
                    if (mPercent != percent) {
                        mPercent = percent;
                        Message msg = new ProgressMessage(HttpMainHandler.PROGRESS_UPDATE,
                                percent,
                                curBytes,
                                contentBytes,
                                fileInfo.getOnProgressCallBack()).
                                build();
                        HttpMainHandler.getInstance().sendMessage(msg);
                    }
                }

                return bytesRead;
            }
        };
    }


}
