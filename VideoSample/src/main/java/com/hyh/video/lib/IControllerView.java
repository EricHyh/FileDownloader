package com.hyh.video.lib;

import android.view.View;

/**
 * @author Administrator
 * @description
 * @data 2019/2/25
 */

public interface IControllerView {

    View getView();

    void setTitle(CharSequence text);

    void setMediaProgress(int progress);

    void setBufferingProgress(int progress);

    void setCurrentPosition(long currentPosition);

    void setDuration(long duration);

    void setStartButtonStartStyle();

    void setStartButtonPauseStyle();

    void setControllerViewClickListener(View.OnClickListener listener);

    void setStartButtonClickListener(View.OnClickListener listener);

    void setReplayButtonClickListener(View.OnClickListener listener);

    void setRetryButtonClickListener(View.OnClickListener listener);

    void setFullScreenToggleClickListener(View.OnClickListener listener);

    void setBackClickListener(View.OnClickListener listener);

    void showControllerView();

    void hideControllerView();

    void showBottomProgress();

    void hideBottomProgress();

    void onFullScreenOpen();

    void onFullScreenClose();

}
