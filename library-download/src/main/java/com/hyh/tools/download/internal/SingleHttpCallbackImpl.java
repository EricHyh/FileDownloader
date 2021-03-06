package com.hyh.tools.download.internal;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.hyh.tools.download.api.Callback;
import com.hyh.tools.download.net.HttpCall;
import com.hyh.tools.download.net.HttpClient;
import com.hyh.tools.download.net.HttpResponse;
import com.hyh.tools.download.bean.Constants;
import com.hyh.tools.download.bean.TaskInfo;
import com.hyh.tools.download.utils.FD_NetUtil;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Administrator
 * @description
 * @data 2017/7/13
 */
@SuppressWarnings("unchecked")
class SingleHttpCallbackImpl extends AbstractHttpCallback {

    private Context context;

    private HttpClient client;

    private TaskInfo taskInfo;

    private HttpCall call;

    private Callback downloadCallback;

    private Timer timer;

    protected volatile boolean pause;

    protected volatile boolean delete;

    //重试的当前次数
    private volatile int currentRetryTimes = 0;
    //重试的最大次数
    private static final int RETRY_MAX_TIMES = 3;
    //每次重试的延时
    private static final long RETRYDELAY = 1000 * 2;
    //获取wifi重试的最大次数
    private static final int SEARCH_WIFI_MAX_TIMES = 15;

    private FileWrite mFileWrite;

    SingleHttpCallbackImpl(Context context, HttpClient client, TaskInfo taskInfo, Callback downloadCallback) {
        this.context = context;
        this.client = client;
        this.taskInfo = taskInfo;
        this.downloadCallback = downloadCallback;
    }


    @Override
    public void onResponse(HttpCall call, HttpResponse response) throws IOException {
        this.call = call;
        if (delete || pause) {
            if (this.call != null && !this.call.isCanceled()) {
                this.call.cancel();
            }
            return;
        }
        int code = response.code();
        taskInfo.setResponseCode(code);
        long contentLength = response.contentLength();
        if (contentLength > 0
                && (code == Constants.ResponseCode.OK || code == Constants.ResponseCode.PARTIAL_CONTENT)) {//请求数据成功
            long totalSize = taskInfo.getTotalSize();
            if (totalSize == 0) {
                taskInfo.setTotalSize(response.contentLength() + taskInfo.getCurrentSize());
            }
            handleDownload(response, taskInfo);
        } else if (contentLength <= 0 && (code == Constants.ResponseCode.OK || code == Constants.ResponseCode.PARTIAL_CONTENT)) {
            //无法获取到文件长度的下载情况，简直坑爹
            taskInfo.setTotalSize(-1L);
            handleDownload(response, taskInfo);
        } else if (code == Constants.ResponseCode.NOT_FOUND) {
            // TODO: 2017/5/16 未找到文件
            downloadCallback.onFailure(taskInfo);
        } else {
            retry();
        }
    }

    @Override
    TaskInfo getTaskInfo() {
        return taskInfo;
    }


    @Override
    public void onFailure(HttpCall call, Exception e) {
        this.call = call;
        retry();
    }

    private void handleDownload(HttpResponse response, final TaskInfo taskInfo) {
        final long currentSize = taskInfo.getCurrentSize();
        final long totalSize = taskInfo.getTotalSize();

        mFileWrite = new SingleFileWriteTask(taskInfo.getFilePath(), currentSize, totalSize);
        mFileWrite.write(response, new SingleFileWriteListener(totalSize, currentSize));
    }


    private class SingleFileWriteListener implements FileWrite.FileWriteListener {

        long totalSize;

        long oldSize;

        int oldProgress;

        public SingleFileWriteListener(long totalSize, long currentSize) {
            this.totalSize = totalSize;
            this.oldSize = currentSize;
            this.oldProgress = (totalSize == -1) ? 0 : (int) ((oldSize * 100.0f / totalSize) + 0.5f);
        }

        @Override
        public void onWriteFile(long writeLength) {
            if (oldSize == 0 && writeLength > 0) {
                downloadCallback.onFirstFileWrite(taskInfo);
            }
            oldSize += writeLength;
            taskInfo.setCurrentSize(oldSize);

            if (totalSize == -1) {
                if (oldProgress != -1) {
                    taskInfo.setProgress(-1);
                    downloadCallback.onDownloading(taskInfo);
                }
                oldProgress = -1;
            } else {
                int progress = (int) ((oldSize * 100.0f / totalSize) + 0.5f);
                if (progress != oldProgress) {
                    currentRetryTimes = 0;
                    taskInfo.setProgress(progress);
                    if (!pause && !delete) {
                        downloadCallback.onDownloading(taskInfo);
                    }
                    oldProgress = progress;
                }
            }
        }

        @Override
        public void onWriteFinish() {
            taskInfo.setProgress(100);
            downloadCallback.onSuccess(taskInfo);
        }

        @Override
        public void onWriteFailure() {
            if (!pause && !delete) {
                retry();
            }
        }
    }


    private void retry() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
        if (pause || delete) {
            return;
        }
        if (currentRetryTimes >= RETRY_MAX_TIMES) {
            //TODO 处理请求失败
            downloadCallback.onFailure(taskInfo);
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            return;
        }
        timer = new Timer("retry timer");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isWifiOk(context)) {
                    if (currentRetryTimes == 0 || currentRetryTimes == 1) {
                        SystemClock.sleep(2 * 1000);
                    }
                    if (currentRetryTimes == 2) {
                        SystemClock.sleep(4 * 1000);
                    }
                    if (pause || delete) {
                        timer.cancel();
                        timer = null;
                        return;
                    }
                    HttpCall call = client.newCall(taskInfo.getResKey(), taskInfo.getUrl(), taskInfo.getCurrentSize());
                    call.enqueue(SingleHttpCallbackImpl.this);
                    timer.cancel();
                    timer = null;
                }
                currentRetryTimes++;

                if (pause || delete) {
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                }
            }
        }, RETRYDELAY);
    }


    private boolean isWifiOk(Context context) {
        int count = 0;
        while (true) {
            if (pause || delete) {
                return false;
            }
            if (FD_NetUtil.isWifi(context)) {
                return true;
            }
            SystemClock.sleep(2000);
            count++;
            if (count == SEARCH_WIFI_MAX_TIMES) {
                return false;
            }
        }
    }

    @Override
    void pause() {
        Log.d("FDL_HH", "SingleHttpCallbackImpl pause");
        this.pause = true;
        if (mFileWrite != null) {
            mFileWrite.stop();
        }
        if (this.call != null && !this.call.isCanceled()) {
            this.call.cancel();
        }
    }

    @Override
    void delete() {
        Log.d("FDL_HH", "SingleHttpCallbackImpl delete");
        this.delete = true;
        if (mFileWrite != null) {
            mFileWrite.stop();
        }
        if (this.call != null && !this.call.isCanceled()) {
            this.call.cancel();
        }
    }
}
