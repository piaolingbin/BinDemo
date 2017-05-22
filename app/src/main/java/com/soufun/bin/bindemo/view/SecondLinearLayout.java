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

public class SecondLinearLayout extends LinearLayout {
    private static final String TAG = "事件传递";

    public SecondLinearLayout(Context context) {
        super(context);
    }

    public SecondLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SecondLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "Second-onTouchEvent");
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG, "Second-dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "Second-onInterceptTouchEvent");
        return super.onInterceptTouchEvent(ev);
    }

}
