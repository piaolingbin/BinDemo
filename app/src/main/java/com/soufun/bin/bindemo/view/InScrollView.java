package com.soufun.bin.bindemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ScrollView;

import static android.R.attr.y;

/**
 * @描述
 * @入口
 * @进入要传的值
 * @备注说明
 */

public class InScrollView extends ScrollView {
    private static final String TAG = "InScrollView";

    private OutScrollView osl;

    public InScrollView(Context context) {
        super(context);
    }

    public InScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOutScrollView(OutScrollView osl){
        this.osl = osl;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                osl.requestDisallowInterceptTouchEvent(true);
                break;

            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ScrollY:" + osl.getScrollY());
                if(osl.getScrollY() < 300){
                    osl.requestDisallowInterceptTouchEvent(true);
                } else {
                    osl.requestDisallowInterceptTouchEvent(false);
                }
                break;

            case MotionEvent.ACTION_UP:

                break;
            default:
                break;

        }
        return super.dispatchTouchEvent(ev);
    }
}
