package com.eric.hyh.tools.download.internal;

import android.content.Context;
import android.os.SystemClock;

import com.eric.hyh.tools.download.api.Callback;
import com.eric.hyh.tools.download.api.HttpCall;
import com.eric.hyh.tools.download.api.HttpClient;
import com.eric.hyh.tools.download.api.HttpResponse;
import com.eric.hyh.tools.download.bean.TaskInfo;

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

    SingleHttpCallbackImpl(Context context, HttpClient client, TaskInfo taskInfo, Callback downloadCallback) {
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
        taskInfo.setCode(code);
        if (code == Constans.ResponseCode.OK || code == Constans.ResponseCode.PARTIAL_CONTENT) {//请求数据成功
            long totalSize = taskInfo.getTotalSize();
            if (totalSize == 0) {
                taskInfo.setTotalSize(response.contentLength() + taskInfo.getCurrentSize());
            }
            handleDownload(response, taskInfo);
        } else if (code == Constans.ResponseCode.NOT_FOUND) {
            // TODO: 2017/5/16 未找到文件
            if (downloadCallback != null) {
                downloadCallback.onFailure(taskInfo);
            }
        } else {
            retry();
        }
    }

    @Override
    TaskInfo getTaskInfo() {
        return taskInfo;
    }


    @Override
    public void onFailure(HttpCall call, IOException e) {
        this.call = call;
        retry();
    }

    private void handleDownload(HttpResponse response, final TaskInfo taskInfo) {
        final long currentSize = taskInfo.getCurrentSize();
        final long totalSize = taskInfo.getTotalSize();

        SingleFileWriteTask singleFileWriteTask = new SingleFileWriteTask(taskInfo.getFilePath(), currentSize, totalSize);
        singleFileWriteTask.write(response, new SingleFileWriteTask.FileWriteListener() {

            long oldSize = currentSize;

            int oldProgress = (int) ((oldSize * 100.0f / totalSize) + 0.5f);

            @Override
            public void onWriteFile(long currentSize) {
                if (oldSize == 0 && currentSize > 0) {
                    if (downloadCallback != null) {
                        downloadCallback.onFirstFileWrite(taskInfo);
                    }
                    taskInfo.setCurrentSize(currentSize);
                    int progress = (int) ((currentSize * 100.0f / totalSize) + 0.5f);
                    if (progress != oldProgress) {
                        currentRetryTimes = 0;
                        taskInfo.setProgress(progress);
                        if (!pause && !delete && downloadCallback != null) {
                            downloadCallback.onDownloading(taskInfo);
                        }
                        oldProgress = progress;
                    }
                }
            }

            @Override
            public void onWriteFinish() {
                if (downloadCallback != null) {
                    downloadCallback.onSuccess(taskInfo);
                }
            }

            @Override
            public void onWriteFailure() {
                if (!pause && !delete) {
                    retry();
                }
            }
        });
    }


    boolean retry() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
        if (pause || delete) {
            return false;
        }
        if (currentRetryTimes >= RETRY_MAX_TIMES) {
            //TODO 处理请求失败
            if (downloadCallback != null) {
                downloadCallback.onFailure(taskInfo);
            }
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            return false;
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
                    //HttpCall call = HttpCallFactory.produce(client, taskInfo);
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
        return true;
    }


    private boolean isWifiOk(Context context) {
        int count = 0;
        while (true) {
            if (pause || delete) {
                return false;
            }
            if (Utils.isWifi(context)) {
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
        this.delete = true;
        if (this.call != null && !this.call.isCanceled()) {
            this.call.cancel();
        }
    }

    @Override
    void delete() {
        this.delete = true;
        if (this.call != null && !this.call.isCanceled()) {
            this.call.cancel();
        }
    }
}