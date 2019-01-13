package com.hyh.download.db.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.hyh.download.DownloadInfo;
import com.hyh.download.db.annotation.Column;
import com.hyh.download.db.annotation.Id;
import com.hyh.download.db.annotation.NotNull;
import com.hyh.download.db.annotation.Unique;
import com.hyh.download.utils.RangeUtil;

/**
 * Created by Administrator on 2017/3/14.
 */

public class TaskInfo implements Parcelable {

    @Id
    @Column(nameInDb = "_id", indexInDb = 0)
    private long id = -1;

    @NotNull
    @Unique
    @Column(nameInDb = "resKey", indexInDb = 1)
    private String resKey;

    @Column(nameInDb = "requestUrl", indexInDb = 2)
    private String requestUrl;

    @Column(nameInDb = "targetUrl", indexInDb = 3)
    private String targetUrl;

    @Column(nameInDb = "cacheRequestUrl", indexInDb = 4)
    private String cacheRequestUrl;

    @Column(nameInDb = "cacheTargetUrl", indexInDb = 5)
    private String cacheTargetUrl;

    @Column(nameInDb = "priority", indexInDb = 6)
    private int priority;

    @NotNull
    @Column(nameInDb = "fileDir", indexInDb = 7)
    private String fileDir;

    @Column(nameInDb = "fileName", indexInDb = 8)
    private String fileName;

    @Column(nameInDb = "byMultiThread", indexInDb = 9)
    private boolean byMultiThread;

    @Column(nameInDb = "rangeNum", indexInDb = 10)
    private int rangeNum;

    @Column(nameInDb = "totalSize", indexInDb = 11)
    private volatile long totalSize;

    @Column(nameInDb = "currentSize", indexInDb = 12)
    private volatile long currentSize;

    @Column(nameInDb = "currentStatus", indexInDb = 13)
    private volatile int currentStatus;

    @Column(nameInDb = "onlyWifiDownload", indexInDb = 14)
    private boolean onlyWifiDownload;

    @Column(nameInDb = "wifiAutoRetry", indexInDb = 15)
    private boolean wifiAutoRetry;

    @Column(nameInDb = "permitRetryInMobileData", indexInDb = 16)
    private boolean permitRetryInMobileData;

    @Column(nameInDb = "permitRetryInvalidFileTask", indexInDb = 17)
    private boolean permitRetryInvalidFileTask;

    @Column(nameInDb = "permitRecoverTask", indexInDb = 18)
    private boolean permitRecoverTask;

    @Column(nameInDb = "responseCode", indexInDb = 19)
    private int responseCode;

    @Column(nameInDb = "failureCode", indexInDb = 20)
    private int failureCode;

    @Column(nameInDb = "contentMD5", indexInDb = 21)
    private String contentMD5;

    @Column(nameInDb = "contentType", indexInDb = 22)
    private String contentType;

    @Column(nameInDb = "eTag", indexInDb = 23)
    private String eTag;

    @Column(nameInDb = "lastModified", indexInDb = 24)
    private String lastModified;

    @Column(nameInDb = "updateTimeMillis", indexInDb = 25)
    private long updateTimeMillis;

    @Column(nameInDb = "tag", indexInDb = 26)
    private String tag;

    public TaskInfo() {
    }

    protected TaskInfo(Parcel in) {
        id = in.readLong();
        resKey = in.readString();
        requestUrl = in.readString();
        targetUrl = in.readString();
        cacheRequestUrl = in.readString();
        cacheTargetUrl = in.readString();
        priority = in.readInt();
        fileDir = in.readString();
        fileName = in.readString();
        byMultiThread = in.readByte() != 0;
        rangeNum = in.readInt();
        totalSize = in.readLong();
        currentSize = in.readLong();
        currentStatus = in.readInt();
        onlyWifiDownload = in.readByte() != 0;
        wifiAutoRetry = in.readByte() != 0;
        permitRetryInMobileData = in.readByte() != 0;
        permitRetryInvalidFileTask = in.readByte() != 0;
        permitRecoverTask = in.readByte() != 0;
        responseCode = in.readInt();
        failureCode = in.readInt();
        contentMD5 = in.readString();
        contentType = in.readString();
        eTag = in.readString();
        lastModified = in.readString();
        updateTimeMillis = in.readLong();
        tag = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(resKey);
        dest.writeString(requestUrl);
        dest.writeString(targetUrl);
        dest.writeString(cacheRequestUrl);
        dest.writeString(cacheTargetUrl);
        dest.writeInt(priority);
        dest.writeString(fileDir);
        dest.writeString(fileName);
        dest.writeByte((byte) (byMultiThread ? 1 : 0));
        dest.writeInt(rangeNum);
        dest.writeLong(totalSize);
        dest.writeLong(currentSize);
        dest.writeInt(currentStatus);
        dest.writeByte((byte) (onlyWifiDownload ? 1 : 0));
        dest.writeByte((byte) (wifiAutoRetry ? 1 : 0));
        dest.writeByte((byte) (permitRetryInMobileData ? 1 : 0));
        dest.writeByte((byte) (permitRetryInvalidFileTask ? 1 : 0));
        dest.writeByte((byte) (permitRecoverTask ? 1 : 0));
        dest.writeInt(responseCode);
        dest.writeInt(failureCode);
        dest.writeString(contentMD5);
        dest.writeString(contentType);
        dest.writeString(eTag);
        dest.writeString(lastModified);
        dest.writeLong(updateTimeMillis);
        dest.writeString(tag);
    }

    public static final Creator<TaskInfo> CREATOR = new Creator<TaskInfo>() {
        @Override
        public TaskInfo createFromParcel(Parcel in) {
            return new TaskInfo(in);
        }

        @Override
        public TaskInfo[] newArray(int size) {
            return new TaskInfo[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskInfo that = (TaskInfo) o;
        return resKey.equals(that.resKey);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getResKey() {
        return resKey;
    }

    public void setResKey(String resKey) {
        this.resKey = resKey;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getCacheRequestUrl() {
        return cacheRequestUrl;
    }

    public void setCacheRequestUrl(String cacheRequestUrl) {
        this.cacheRequestUrl = cacheRequestUrl;
    }

    public String getCacheTargetUrl() {
        return cacheTargetUrl;
    }

    public void setCacheTargetUrl(String cacheTargetUrl) {
        this.cacheTargetUrl = cacheTargetUrl;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isByMultiThread() {
        return byMultiThread;
    }

    public void setByMultiThread(boolean byMultiThread) {
        this.byMultiThread = byMultiThread;
    }

    public int getRangeNum() {
        return rangeNum;
    }

    public void setRangeNum(int rangeNum) {
        this.rangeNum = rangeNum;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
    }

    public int getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(int currentStatus) {
        this.currentStatus = currentStatus;
    }

    public boolean isOnlyWifiDownload() {
        return onlyWifiDownload;
    }

    public void setOnlyWifiDownload(boolean onlyWifiDownload) {
        this.onlyWifiDownload = onlyWifiDownload;
    }

    public boolean isWifiAutoRetry() {
        return wifiAutoRetry;
    }

    public void setWifiAutoRetry(boolean wifiAutoRetry) {
        this.wifiAutoRetry = wifiAutoRetry;
    }

    public boolean isPermitRetryInMobileData() {
        return permitRetryInMobileData;
    }

    public void setPermitRetryInMobileData(boolean permitRetryInMobileData) {
        this.permitRetryInMobileData = permitRetryInMobileData;
    }

    public boolean isPermitRetryInvalidFileTask() {
        return permitRetryInvalidFileTask;
    }

    public void setPermitRetryInvalidFileTask(boolean permitRetryInvalidFileTask) {
        this.permitRetryInvalidFileTask = permitRetryInvalidFileTask;
    }

    public boolean isPermitRecoverTask() {
        return permitRecoverTask;
    }

    public void setPermitRecoverTask(boolean permitRecoverTask) {
        this.permitRecoverTask = permitRecoverTask;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getFailureCode() {
        return failureCode;
    }

    public void setFailureCode(int failureCode) {
        this.failureCode = failureCode;
    }

    public String getContentMD5() {
        return contentMD5;
    }

    public void setContentMD5(String contentMD5) {
        this.contentMD5 = contentMD5;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getETag() {
        return eTag;
    }

    public void setETag(String eTag) {
        this.eTag = eTag;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public long getUpdateTimeMillis() {
        return updateTimeMillis;
    }

    public void setUpdateTimeMillis(long updateTimeMillis) {
        this.updateTimeMillis = updateTimeMillis;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public DownloadInfo toDownloadInfo() {
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setResKey(this.resKey);
        downloadInfo.setRequestUrl(this.requestUrl);
        downloadInfo.setTargetUrl(this.targetUrl);
        downloadInfo.setPriority(this.priority);
        downloadInfo.setFileDir(this.fileDir);
        downloadInfo.setFileName(this.fileName);

        downloadInfo.setByMultiThread(this.byMultiThread);
        downloadInfo.setOnlyWifiDownload(this.onlyWifiDownload);
        downloadInfo.setWifiAutoRetry(this.wifiAutoRetry);
        downloadInfo.setPermitRetryInMobileData(this.permitRetryInMobileData);
        downloadInfo.setPermitRetryInvalidFileTask(this.permitRetryInvalidFileTask);
        downloadInfo.setPermitRecoverTask(this.permitRecoverTask);

        downloadInfo.setTotalSize(this.totalSize);
        downloadInfo.setCurrentSize(this.currentSize);
        downloadInfo.setProgress(RangeUtil.computeProgress(this.currentSize, this.totalSize));

        downloadInfo.setCurrentStatus(this.currentStatus);

        downloadInfo.setResponseCode(this.responseCode);
        downloadInfo.setFailureCode(this.failureCode);
        downloadInfo.setContentType(this.contentType);
        downloadInfo.setTag(this.tag);
        return downloadInfo;
    }
}
