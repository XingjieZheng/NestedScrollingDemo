# NestedScrollingDemo
Android NestedScrolling
### 效果显示如下
![image](https://github.com/XingjieZheng/NestedScrollingDemo/blob/master/assets/NestedScrollingDemoPreview.gif)


该工程是通过使用 NestedScorllingParent 嵌套 ListView 实现滑动动画功能，如下为项目要点分析

### 1.Android nested scrolling 原理
参考这篇文章 https://segmentfault.com/a/1190000002873657

### 2.注意要点
由于ListView 继承 AbsListView，而在Android5.0 api中AbsListView 中添加了NestedScrolling 交互机制，如下
```java
@Override
public boolean onTouchEvent(MotionEvent ev) { 
   ···
   startNestedScroll(SCROLL_AXIS_VERTICAL);
   ···
   }     

private void scrollIfNeeded(int x, int y, MotionEvent vtev) {
        ···
        if (dispatchNestedPreScroll(0, mLastY != Integer.MIN_VALUE ? mLastY - y : -rawDeltaY,
                mScrollConsumed, mScrollOffset)) {
        ···
}
```
等等方法。而Android5.0以下的AbsListView则没有这个，所以在NestedScrollChildView中的onTouchEvent中添加NestedSrolling机制，如下
```java
@Override
    public boolean onTouchEvent(MotionEvent e) {
        if (!isVersionOver20()) {
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
	···
        }
        return super.onTouchEvent(e);
    }
```
