package cn.jzvd.demo;

import android.app.Activity;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * Created by Nathen on 2017/9/19.
 */

public class ActivityApiExtendsNormal extends Activity {

    private JzvdStd mJzvdStd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extends_normal);
        mJzvdStd = findViewById(R.id.videoplayer);
        mJzvdStd.setUp(VideoConstant.videoUrlList[0], "饺子不信"
                , JzvdStd.SCREEN_WINDOW_NORMAL);
        Glide.with(this)
                .load(VideoConstant.videoThumbList[0])
                .into(mJzvdStd.thumbImageView);
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }
}
