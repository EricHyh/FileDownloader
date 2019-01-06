package com.hyh.download;

/**
 * Created by Administrator on 2017/3/10.
 */

public class State {

    public static final int NONE = 0;//初始状态，数据库中还未存储，不用处理

    public static final int PREPARE = 1;//准备状态，显示下载中

    public static final int WAITING_IN_QUEUE = 2;//等待状态

    public static final int CONNECTED = 3;//已连接文件服务器

    public static final int DOWNLOADING = 4;//下载中状态，显示下载中

    public static final int RETRYING = 5;//重试中状态，显示下载中

    public static final int PAUSE = 6;//下载任务被暂停

    public static final int DELETE = 7;//下载任务被删除

    public static final int SUCCESS = 8;//下载成功

    public static final int FAILURE = 9;//下载失败

}
