package com.soufun.bin.bindemo.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * @描述
 * @入口
 * @进入要传的值
 * @备注说明
 */

public class OutScrollView extends ScrollView {
    private static final String TAG = "OutScrollView";
    public OutScrollView(Context context) {
        super(context);
    }

    public OutScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OutScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    int mLastX = 0;
    int mLastY = 0;
   @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
       Log.d(TAG, "scrollY :" + getScrollY());
       int x = (int) ev.getX();
       int y = (int) ev.getY();
       boolean intercept = false;
       switch (ev.getAction()){
           case MotionEvent.ACTION_DOWN:
               intercept = false;
               break;
           case MotionEvent.ACTION_MOVE:
               intercept = true;
               break;
           case MotionEvent.ACTION_UP:
               intercept = true;
               break;
       }
       if(intercept){
           scrollBy(0, y - mLastY);
       }
       mLastY = y;
        return intercept;
    }

}
