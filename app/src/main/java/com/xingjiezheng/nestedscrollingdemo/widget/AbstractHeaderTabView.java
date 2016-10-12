package com.xingjiezheng.nestedscrollingdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by XingjieZheng
 * on 2016/9/21.
 */
public abstract class AbstractHeaderTabView extends FrameLayout {

    protected int mHeaderHeight;
    protected int mHeaderYScroll;
    protected int mHeaderYScrollRange;
    protected int mFixHeight;
    protected boolean isScrollUp;
    protected HeaderTabToolBarView mHeaderTabToolBarView;

    public AbstractHeaderTabView(Context context) {
        super(context);
    }

    public AbstractHeaderTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbstractHeaderTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public abstract int getHeaderTop();

    public abstract int moveBy(int deltaY);

    public int getHeaderYScroll() {
        return mHeaderYScroll;
    }

    public int getHeaderYScrollRange() {
        return mHeaderYScrollRange;
    }

    public int getFixHeight() {
        return mFixHeight;
    }

    public int getHeaderHeight() {
        return mHeaderHeight;
    }

    public boolean isScrollUp() {
        return isScrollUp;
    }

    public void setToolBarViewToHeaderView(HeaderTabToolBarView headerTabToolBarView) {
        this.mHeaderTabToolBarView = headerTabToolBarView;
    }
}
