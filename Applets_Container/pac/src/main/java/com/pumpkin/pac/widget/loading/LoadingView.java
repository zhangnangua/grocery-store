package com.pumpkin.pac.widget.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

public class LoadingView extends AppCompatImageView {
    private LoadingDrawable mLoadingDrawable;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setLoadingRenderer(new ElectricFanLoadingRenderer(context));
    }

    public void setLoadingRenderer(LoadingRenderer loadingRenderer) {
        mLoadingDrawable = new LoadingDrawable(loadingRenderer);
        setImageDrawable(mLoadingDrawable);
    }

//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        startAnimation();
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        stopAnimation();
//    }
//
//    @Override
//    protected void onVisibilityChanged(View changedView, int visibility) {
//        super.onVisibilityChanged(changedView, visibility);
//
//        final boolean visible = visibility == VISIBLE && getVisibility() == VISIBLE;
//        if (visible) {
//            startAnimation();
//        } else {
//            stopAnimation();
//        }
//    }

    public void progress(int progress) {
        mLoadingDrawable.change(progress);
        invalidate();
    }
}