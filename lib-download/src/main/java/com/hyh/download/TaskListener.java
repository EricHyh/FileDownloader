package com.hyh.download;

import java.util.List;
import java.util.Map;

/**
 * Created by Eric_He on 2019/1/1.
 */

public interface TaskListener {

    void onPrepare(DownloadInfo downloadInfo);

    void onWaitingStart(DownloadInfo downloadInfo);

    void onWaitingEnd(DownloadInfo downloadInfo);

    void onConnected(DownloadInfo downloadInfo, Map<String, List<String>> responseHeaderFields);

    void onDownloading(String resKey, long totalSize, long currentSize, int progress, float speed);

    void onRetrying(DownloadInfo downloadInfo, boolean deleteFile);

    void onPause(DownloadInfo downloadInfo);

    void onDelete(DownloadInfo downloadInfo);

    void onSuccess(DownloadInfo downloadInfo);

    void onFailure(DownloadInfo downloadInfo);

}
