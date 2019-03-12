package com.hyh.video.lib;

import android.view.View;

/**
 * @author Administrator
 * @description
 * @data 2019/1/28
 */

public interface IVideoController {

    View getView();

    void setup(HappyVideo happyVideo, CharSequence title, IMediaInfo mediaInfo);

    boolean interceptPrepare(boolean autoStart);

    boolean interceptStart();

    boolean interceptRestart();

    boolean interceptRetry();

}