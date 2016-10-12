package com.xingjiezheng.nestedscrollingdemo.widget;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ListView;

/**
 * Created by XingjieZheng
 * on 2016/9/12.
 */
public class NestedScrollChildView extends ListView implements NestedScrollingChild {

    private static final String TAG = NestedScrollChildView.class.getSimpleName();
    private static final int INVALID_POINTER = -1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_DRAGGING = 1;

    private NestedScrollingChildHelper mChildHelper;
    private int mScrollPointerId = INVALID_POINTER;
    private int mLastTouchX;
    private int mLastTouchY;
    private int mTouchSlop;
    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];
    private int mScrollState = SCROLL_STATE_IDLE;

    public NestedScrollChildView(Context context) {
        this(context, null);
    }

    public NestedScrollChildView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);

        final ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (!isVersionOver19()) {
            final MotionEvent vtev = MotionEvent.obtain(e);
            final int action = MotionEventCompat.getActionMasked(e);
            final int actionIndex = MotionEventCompat.getActionIndex(e);

            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    mScrollPointerId = e.getPointerId(0);
                    mLastTouchX = (int) (e.getX() + 0.5f);
                    mLastTouchY = (int) (e.getY() + 0.5f);
                    startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                }
                break;

                case MotionEventCompat.ACTION_POINTER_DOWN: {
                    mScrollPointerId = e.getPointerId(actionIndex);
                    mLastTouchX = (int) (e.getX(actionIndex) + 0.5f);
                    mLastTouchY = (int) (e.getY(actionIndex) + 0.5f);
                }
                break;

                case MotionEvent.ACTION_MOVE: {
                    final int index = e.findPointerIndex(mScrollPointerId);
                    if (index < 0) {
                        Log.e(TAG, "Error processing scroll; pointer index for id " +
                                mScrollPointerId + " not found. Did any MotionEvents get skipped?");
//                        return false;
                        break;
                    }

                    final int x = (int) (e.getX(index) + 0.5f);
                    final int y = (int) (e.getY(index) + 0.5f);
                    int dx = mLastTouchX - x;
                    int dy = mLastTouchY - y;

                    dispatchNestedPreScroll(dx, dy, mScrollConsumed, mScrollOffset);

                    if (mScrollState != SCROLL_STATE_DRAGGING && Math.abs(dy) > mTouchSlop) {
                        mScrollState = SCROLL_STATE_DRAGGING;
                    }

                    if (mScrollState == SCROLL_STATE_DRAGGING) {
                        mLastTouchX = x;
                        mLastTouchY = y;
                    }
                }
                break;

                case MotionEventCompat.ACTION_POINTER_UP: {
                    onPointerUp(e);
                }
                break;

                case MotionEvent.ACTION_UP: {
                    stopNestedScroll();
                }
                break;

                case MotionEvent.ACTION_CANCEL: {
                    cancelTouch();
                }
                break;
            }
            vtev.recycle();
        }
        return super.onTouchEvent(e);
    }

    private boolean isVersionOver19() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    private void onPointerUp(MotionEvent e) {
        final int actionIndex = MotionEventCompat.getActionIndex(e);
        if (e.getPointerId(actionIndex) == mScrollPointerId) {
            // Pick a new pointer to pick up the slack.
            final int newIndex = actionIndex == 0 ? 1 : 0;
            mScrollPointerId = e.getPointerId(newIndex);
            mLastTouchX = (int) (e.getX(newIndex) + 0.5f);
            mLastTouchY = (int) (e.getY(newIndex) + 0.5f);
        }
    }

    private void cancelTouch() {
        stopNestedScroll();
        mScrollState = SCROLL_STATE_IDLE;
    }

}
