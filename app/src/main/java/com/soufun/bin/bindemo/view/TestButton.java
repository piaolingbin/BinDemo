package com.soufun.bin.bindemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * @描述
 * @入口
 * @进入要传的值
 * @备注说明
 */

public class TestButton extends TextView implements GestureDetector.OnDoubleTapListener {
    public TestButton(Context context) {
        super(context);
    }

    public TestButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 实际开发中可以不使用GestureDetector，在onTouchEvent中就可以实现所需监听。
    // 建议：如果只是滑动监听，在onTouchEvent就完成即可；而如果要监听双击等行为，最好用GestureDetector
    GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            // 手指轻触屏幕的一瞬间
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // 手指轻触屏幕，尚未松开或拖动
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // 手指轻触屏幕后松开，伴随一个ACTION_UP触发，这是一个单击行为
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // 手指按下屏幕并拖动，伴随1个ACTION_DOWN和多个ACTION_MOVE触发，这是拖动行为
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // 长按
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // 按下屏幕，快速滑动后松开。快速滑动行为
            return false;
        }
    });

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TouchSlop 系统所能识别出的最小滑动距离
        int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        // 触摸点
            // 相对于当前view左上角
        event.getX();
        event.getY();
            // 相对于屏幕左上角
        event.getRawX();
        event.getRawY();

        // 当前控件 left、top、right、bottom代表控件初始位置，平移控件是不会改变他们的值的
            // 左上角相对于父容器左上角
        getLeft();
        getTop();
            // 右下角相对于父容器左上角
        getRight();
        getBottom();

        // 以下4个参数Android3.0才加的
        // 控件平移过程中控件左上角相对于父容器的偏移量
        getTranslationX();
        getTranslationY();
        // 控件平移后左上角的坐标
        getX(); // x = left + translationX;
        getY(); // y = top + translationY;

        // GestureDetector接管该view的onTouchEvent方法
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }



    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        // 严格的单击，在它之后不可能在紧跟这另一个单击，即这只是一个单击，而不是双击中的一次单击。
        // onSingleTagUp:一次单击。在一次双击中有两个连续的onSingleTagUp
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        // 双击，不可能和onSingleTapConfirmed共存
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    // 弹性滑动
    Scroller scroller = new Scroller(getContext());

    private void smoothScrollTo(int destX , int destY){
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        // 1000ms内滑向destY,效果就是慢慢滑动
        scroller.startScroll(scrollX , 0 , delta , 0 , 1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX() , scroller.getCurrY());
            postInvalidate();
        }
        super.computeScroll();
    }
}
