package com.hyh.download.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;

import com.hyh.download.Callback;
import com.hyh.download.DownloadInfo;
import com.hyh.download.DownloaderConfig;
import com.hyh.download.IClient;
import com.hyh.download.IFileChecker;
import com.hyh.download.IRequest;
import com.hyh.download.State;
import com.hyh.download.core.service.FDLService;
import com.hyh.download.db.bean.TaskInfo;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/3/9.
 */

public class ServiceBridge implements IDownloadProxy {


    private final ThreadPoolExecutor mExecutor;

    {
        int corePoolSize = 1;
        int maximumPoolSize = 1;
        long keepAliveTime = 120L;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "FileDownloader");
                thread.setDaemon(true);
                return thread;
            }
        };
        RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();
        mExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }


    private final Object mServiceConnectedLock = new Object();

    private volatile boolean mIsConnected = false;
    private volatile boolean mIsStartBind = false;
    private volatile ServiceConnection mConnection;
    private Context mContext;
    private IRequest mServiceAgent;
    private Callback mCallback;
    private Map<String, TaskCache> mCacheTasks = new LinkedHashMap<>();

    private DownloaderConfig mDownloaderConfig;
    private final int mPid;
    private Class mServiceClass;

    public ServiceBridge(Context context, DownloaderConfig downloaderConfig, Callback callback) {
        this.mContext = context.getApplicationContext();
        this.mServiceClass = downloaderConfig.isIndependentProcess() ?
                FDLService.IndependentProcessService.class : FDLService.MainProcessService.class;
        this.mDownloaderConfig = downloaderConfig;
        this.mCallback = callback;
        this.mPid = Process.myPid();
    }

    @Override
    public void initProxy(Runnable afterInit) {
        waitingForService();
        try {
            mServiceAgent.initDownloadProxy(
                    new DownloadProxyConfig(mDownloaderConfig.getMaxSyncDownloadNum(), mDownloaderConfig.getThreadMode()),
                    mDownloaderConfig.getGlobalFileChecker());
            afterInit.run();
        } catch (Exception e) {
            //
        }
    }

    private synchronized void bindService() {
        if (isServiceAlive()) {
            return;
        }
        if (mIsStartBind) {
            return;
        }
        mIsStartBind = true;
        if (mConnection == null) {
            mConnection = getConnection();
        }
        Intent intent = new Intent(mContext, mServiceClass);
        mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mContext.startService(intent);
    }

    @Override
    public synchronized void startTask(TaskInfo taskInfo, IFileChecker fileChecker) {
        waitingForService();
        try {
            String resKey = taskInfo.getResKey();
            mCacheTasks.put(resKey, new TaskCache(Command.START, taskInfo, fileChecker));
            mServiceAgent.startTask(taskInfo, fileChecker);
            mCacheTasks.remove(resKey);
        } catch (Exception e) {
            //
        }
    }

    @Override
    public synchronized void pauseTask(String resKey) {
        waitingForService();
        try {

            mServiceAgent.pauseTask(resKey);
        } catch (Exception e) {
            //
        }
    }

    @Override
    public synchronized void deleteTask(String resKey) {
        waitingForService();

    }

    @Override
    public boolean isTaskAlive(String resKey) {
        waitingForService();
        try {
            return mServiceAgent.isTaskAlive(resKey);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isFileDownloaded(String resKey, IFileChecker fileChecker) {
        waitingForService();
        try {
            return mServiceAgent.isFileDownloaded(resKey, fileChecker);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isFileDownloaded(String resKey, int versionCode, IFileChecker fileChecker) {
        waitingForService();
        try {
            return mServiceAgent.isFileDownloadedWithVersion(resKey, versionCode, fileChecker);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public TaskInfo getTaskInfoByKey(String resKey) {
        waitingForService();
        try {
            return mServiceAgent.getTaskInfoByKey(resKey);
        } catch (Exception e) {
            return null;
        }
    }

    private void waitingForService() {
        if (isServiceAlive()) {
            return;
        }
        bindService();
        synchronized (mServiceConnectedLock) {
            while (true) {
                try {
                    mServiceConnectedLock.wait();
                } catch (Exception e) {
                    //
                }
                if (isServiceAlive()) {
                    break;
                }
            }
        }
    }

    private boolean isServiceAlive() {
        if (mIsConnected && mServiceAgent != null) {
            try {
                return mServiceAgent.isAlive();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void insertOrUpdate(TaskInfo taskInfo) {
        waitingForService();
        try {
            mServiceAgent.insertOrUpdate(taskInfo);
        } catch (RemoteException e) {
            //
        }
    }

    private IClient.Stub mClient = new IClient.Stub() {

        @Override
        public boolean isAlive() throws RemoteException {
            return true;
        }

        @Override
        public void onCallback(DownloadInfo downloadInfo) throws RemoteException {
            if (downloadInfo == null) {
                return;
            }
            switch (downloadInfo.getCurrentStatus()) {
                case State.PREPARE: {
                    if (mCallback != null) {
                        mCallback.onPrepare(downloadInfo);
                    }
                    break;
                }
                case State.WAITING_IN_QUEUE: {
                    if (mCallback != null) {
                        mCallback.onWaitingInQueue(downloadInfo);
                    }
                    break;
                }
                case State.DOWNLOADING: {
                    if (mCallback != null) {
                        mCallback.onDownloading(downloadInfo);
                    }
                    break;
                }
                case State.PAUSE: {
                    if (mCallback != null) {
                        mCallback.onPause(downloadInfo);
                    }
                    break;
                }
                case State.DELETE: {
                    if (mCallback != null) {
                        mCallback.onDelete(downloadInfo);
                    }
                    break;
                }
                case State.SUCCESS: {
                    if (mCallback != null) {
                        mCallback.onSuccess(downloadInfo);
                    }
                    break;
                }
                case State.WAITING_FOR_WIFI: {
                    if (mCallback != null) {
                        mCallback.onWaitingForWifi(downloadInfo);
                    }
                    break;
                }
                case State.LOW_DISK_SPACE: {
                    if (mCallback != null) {
                        mCallback.onLowDiskSpace(downloadInfo);
                    }
                    break;
                }
                case State.FAILURE: {
                    if (mCallback != null) {
                        mCallback.onFailure(downloadInfo);
                    }
                    break;
                }
            }
        }
    };

    private ServiceConnection getConnection() {
        return mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(final ComponentName name, IBinder service) {
                mServiceAgent = IRequest.Stub.asInterface(service);
                try {
                    mServiceAgent.asBinder().linkToDeath(new IBinder.DeathRecipient() {
                        @Override
                        public void binderDied() {
                            onServiceDisconnected(name);
                        }
                    }, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                mIsConnected = true;
                mIsStartBind = false;
                try {
                    mServiceAgent.register(mPid, mClient);
                    synchronized (mServiceConnectedLock) {
                        mServiceConnectedLock.notifyAll();
                    }
                    if (mCacheTasks.size() > 0) {
                        Collection<TaskCache> values = mCacheTasks.values();
                        for (TaskCache taskCache : values) {
                            int command = taskCache.command;
                            switch (command) {
                                case Command.START: {
                                    startTask(taskCache.taskInfo, taskCache.fileChecker);
                                    break;
                                }
                                case Command.PAUSE: {
                                    pauseTask(taskCache.resKey);
                                    break;
                                }
                                case Command.DELETE: {
                                    deleteTask(taskCache.resKey);
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mConnection = null;
                mIsConnected = false;
                mIsStartBind = false;
                bindService();
            }
        };
    }
}
