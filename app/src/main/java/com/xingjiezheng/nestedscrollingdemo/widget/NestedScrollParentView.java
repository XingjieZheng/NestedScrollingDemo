package com.xingjiezheng.nestedscrollingdemo.widget;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by XingjieZheng
 * on 2016/9/9.
 */
public class NestedScrollParentView extends ViewGroup implements NestedScrollingParent {

    private NestedScrollingParentHelper nestedScrollingParentHelper;
    private AbstractHeaderTabView mHeaderView;
    private View mContentView;
    private HeaderTabToolBarView mHeaderTabToolBarView;

    private OnOffsetChangedListener onOffsetChangedListener;

    public NestedScrollParentView(Context context) {
        this(context, null, 0);
    }

    public NestedScrollParentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedScrollParentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
    }

    @Override
    protected void onFinishInflate() {
        final int childCount = getChildCount();
        if (childCount == 3) {
            View child1 = getChildAt(0);
            View child2 = getChildAt(1);
            View child3 = getChildAt(2);
            if (child1 instanceof AbstractHeaderTabView) {
                mHeaderView = (AbstractHeaderTabView) child1;
                if (child2 instanceof HeaderTabToolBarView) {
                    mHeaderTabToolBarView = (HeaderTabToolBarView) child2;
                    mContentView = child3;
                } else if (child3 instanceof HeaderTabToolBarView) {
                    mHeaderTabToolBarView = (HeaderTabToolBarView) child3;
                    mContentView = child2;
                } else {
                    showException();
                }
            } else if (child2 instanceof AbstractHeaderTabView) {
                mHeaderView = (AbstractHeaderTabView) child2;
                if (child1 instanceof HeaderTabToolBarView) {
                    mHeaderTabToolBarView = (HeaderTabToolBarView) child1;
                    mContentView = child3;
                } else if (child3 instanceof HeaderTabToolBarView) {
                    mHeaderTabToolBarView = (HeaderTabToolBarView) child3;
                    mContentView = child1;
                } else {
                    showException();
                }
            } else if (child3 instanceof AbstractHeaderTabView) {
                mHeaderView = (AbstractHeaderTabView) child3;
                if (child1 instanceof HeaderTabToolBarView) {
                    mHeaderTabToolBarView = (HeaderTabToolBarView) child1;
                    mContentView = child2;
                } else if (child2 instanceof HeaderTabToolBarView) {
                    mHeaderTabToolBarView = (HeaderTabToolBarView) child2;
                    mContentView = child1;
                } else {
                    showException();
                }
            } else {
                showException();
            }
        } else {
            throw new IllegalStateException("NestedScrollParentView only can host 3 elements");
        }
        setHeaderViewOffsetListener();
        super.onFinishInflate();
    }

    private void showException() {
        throw new IllegalStateException("Tow of nestedScrollParentView`s children must be instanceof IHeaderTabView and HeaderTabTooBarView");
    }

    private void setHeaderViewOffsetListener() {
        if (mHeaderView instanceof OnOffsetChangedListener) {
            setOnOffsetChangedListener((OnOffsetChangedListener) mHeaderView);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
        }

        if (mContentView != null) {
            measureChild(mContentView, widthMeasureSpec, heightMeasureSpec);
        }

        if (mHeaderTabToolBarView != null) {
            measureChild(mHeaderTabToolBarView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChildren();
    }

    private void layoutChildren() {
        if (mHeaderView == null || mContentView == null || mHeaderTabToolBarView == null) {
            return;
        }
        int headerLeft = getPaddingLeft() + mHeaderView.getPaddingLeft();
        int headerTop = mHeaderView.getHeaderTop() + getPaddingTop();
        int headerRight = getRight() - getPaddingRight() - mHeaderView.getPaddingRight();
        int headerBottom = headerTop + mHeaderView.getMeasuredHeight();
        mHeaderView.layout(headerLeft, headerTop, headerRight, headerBottom);

        mContentView.setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom() + mHeaderView.getFixHeight());
        int contentLeft = getPaddingLeft() + mContentView.getPaddingLeft();
        int contentTop = mContentView.getPaddingTop() + headerBottom + mHeaderView.getPaddingBottom();
        int contentRight = getRight() - getPaddingRight() - mContentView.getPaddingRight();
        int contentBottom = contentTop + mContentView.getMeasuredHeight();
        mContentView.layout(contentLeft, contentTop, contentRight, contentBottom);

        int headerToolBarLeft = getPaddingLeft() + mHeaderTabToolBarView.getPaddingLeft();
        int headerToolBarTop = getPaddingTop() + mHeaderTabToolBarView.getPaddingTop();
        int headerToolBarRight = getRight() - getPaddingRight() - mHeaderTabToolBarView.getPaddingRight();
        int headerToolBarBottom = headerToolBarTop + mHeaderView.getMeasuredHeight();
        mHeaderTabToolBarView.layout(headerToolBarLeft, headerToolBarTop, headerToolBarRight, headerToolBarBottom);
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        nestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public int getNestedScrollAxes() {
        return nestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(View child) {
        nestedScrollingParentHelper.onStopNestedScroll(child);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }


    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        moveBy(dy);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    private int moveBy(int deltaY) {
        if (mHeaderView == null) {
            return 0;
        }
        int headerMove = mHeaderView.moveBy(deltaY);
        if (headerMove != 0) {
            if (onOffsetChangedListener != null) {
                onOffsetChangedListener.onOffsetChanged(mHeaderView, mHeaderView.getHeaderYScroll(),
                        headerMove, mHeaderView.getHeaderYScrollRange(), mHeaderView.isScrollUp());
            }
            if (mContentView != null) {
                mContentView.offsetTopAndBottom(headerMove);
            }
        }
        return headerMove;
    }

    public interface OnOffsetChangedListener {
        void onOffsetChanged(View headerView, int offsetPosition, int offsetDistance, int offsetRange, boolean isUp);
    }

    private void setOnOffsetChangedListener(OnOffsetChangedListener onOffsetChangedListener) {
        this.onOffsetChangedListener = onOffsetChangedListener;
    }
}
