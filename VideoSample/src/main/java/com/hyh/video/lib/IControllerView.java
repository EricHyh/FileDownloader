package com.hyh.video.lib;

import android.view.View;
import android.widget.SeekBar;

/**
 * @author Administrator
 * @description
 * @data 2019/2/25
 */

public interface IControllerView {

    View getView();

    void setup(VideoDelegate videoDelegate, CharSequence title, IMediaInfo mediaInfo);

    void setMediaProgress(int progress);

    void setBufferingProgress(int progress);

    void setCurrentPosition(long currentPosition);

    void setDuration(long duration);

    void setPlayStyle();

    void setPauseStyle();

    void setControllerViewClickListener(View.OnClickListener listener);

    void setPlayOrPauseClickListener(View.OnClickListener listener);

    void setReplayClickListener(View.OnClickListener listener);

    void setRetryClickListener(View.OnClickListener listener);

    void setFullScreenToggleClickListener(View.OnClickListener listener);

    void setMobileDataConfirmClickListener(View.OnClickListener listener);

    void setFullscreenBackClickListener(View.OnClickListener listener);

    void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener listener);

    void showInitialView();

    void hideInitialView();

    void showMobileDataConfirm();

    void hideMobileDataConfirm();

    boolean isShowOperateView();

    void showOperateView();

    void hideOperateView();

    void showEndView();

    void hideEndView();

    void showErrorView();

    void hideErrorView();

    void showLoadingView();

    void showLoadingViewDelayed(long delayMillis);

    void hideLoadingView();

}