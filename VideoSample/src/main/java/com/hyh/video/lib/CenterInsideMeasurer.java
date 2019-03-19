package com.hyh.video.lib;

/**
 * @author Administrator
 * @description
 * @data 2019/3/8
 */

public class CenterInsideMeasurer implements ISurfaceMeasurer {

    private final int[] mMeasureSize = new int[2];

    @Override
    public int[] onMeasure(int defaultWidth, int defaultHeight, int videoWidth, int videoHeight) {
        if (defaultWidth == 0 || defaultHeight == 0 || videoWidth == 0 || videoHeight == 0) {
            mMeasureSize[0] = defaultWidth;
            mMeasureSize[1] = defaultHeight;
            return mMeasureSize;
        }
        float ratio = defaultHeight * 1.0f / defaultWidth;
        float videoRatio = videoHeight * 1.0f / videoWidth;
        if (videoRatio > ratio) {
            if (videoHeight > defaultHeight) {
                mMeasureSize[1] = defaultHeight;
            } else {
                mMeasureSize[1] = videoHeight;
            }
            mMeasureSize[0] = Math.round(mMeasureSize[1] / videoRatio);
        } else {
            if (videoWidth > defaultWidth) {
                mMeasureSize[0] = defaultWidth;
            } else {
                mMeasureSize[0] = videoWidth;
            }
            mMeasureSize[1] = Math.round(mMeasureSize[0] * videoRatio);
        }
        return mMeasureSize;
    }
}