package com.soufun.bin.bindemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * @描述
 * @入口
 * @进入要传的值
 * @备注说明
 */

public class FirstLinearLayout extends LinearLayout {
    private static final String TAG = "事件传递";

    public FirstLinearLayout(Context context) {
        super(context);
    }

    public FirstLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FirstLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "First-onTouchEvent");
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG, "First-dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "First-onInterceptTouchEvent");
        return super.onInterceptTouchEvent(ev);
    }
}
