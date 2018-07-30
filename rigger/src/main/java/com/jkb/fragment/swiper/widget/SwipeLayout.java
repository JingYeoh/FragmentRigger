package com.jkb.fragment.swiper.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.jkb.fragment.rigger.rigger.Rigger;
import com.jkb.fragment.swiper.annotation.SwipeEdge;
import java.util.Stack;

/**
 * The widget support {@link android.app.Activity}/{@link android.support.v4.app.Fragment} to exit by swipe edge.
 * <p>
 * This widget can only used with Rigger lib. please do not use it in your layout.
 *
 * @author JingYeoh
 * <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 * <a href="https://github.com/justkiddingbaby">Github</a>
 * <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Feb 09,2018.
 */
public class SwipeLayout extends FrameLayout {

    // attributes
    private boolean mIsEnable;
    @FloatRange(from = 0.0f, to = 1.0f)
    private float mParallaxOffset;
    private SwipeEdge[] mSwipeEdgeSide;
    private boolean mIsStickyWithHost;
    private int mEdgeWidthOffset;

    private Object mPuppet;
    private ViewDragHelper mDragHelper;

    private int mLastX;
    private int mLastY;
    private int mDX;
    private int mDY;
    private int mLastXIntercept;
    private int mLastYIntercept;
    private int mEdgeFlag;

    public SwipeLayout(@NonNull Context context) {
        this(context, null);
    }

    public SwipeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragCallback());
    }

    public void setPuppet(@NonNull Object puppet) {
        mPuppet = puppet;
    }

    @Override
    public void addView(View child) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("SwipeLayout can host only one direct child");
        }

        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("SwipeLayout can host only one direct child");
        }

        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("SwipeLayout can host only one direct child");
        }

        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("SwipeLayout can host only one direct child");
        }

        super.addView(child, index, params);
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            mEdgeFlag
            if (canSwipe(SwipeEdge.LEFT, SwipeEdge.RIGHT)) {

            }
            boolean dragEnable = mDragHelper.isEdgeTouched(mEdgeFlag, pointerId);
            return false;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return super.clampViewPositionHorizontal(child, left, dx);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return super.clampViewPositionVertical(child, top, dy);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!mIsEnable || canSwipe(SwipeEdge.NONE)) return super.onInterceptTouchEvent(event);
        try {
            mDragHelper.shouldInterceptTouchEvent(event);
            return true;
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsEnable || canSwipe(SwipeEdge.NONE)) return super.onTouchEvent(event);
        try {
            mDragHelper.processTouchEvent(event);
            return true;
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return false;
    }

    private boolean onInterceptMoved(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int deltaX = (int) x - mLastXIntercept;
        int deltaY = (int) y - mLastYIntercept;
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            return canSwipe(SwipeEdge.LEFT, SwipeEdge.RIGHT) &&
                (x <= mEdgeWidthOffset || x >= getMeasuredWidth() - mEdgeWidthOffset);
        } else {
            return canSwipe(SwipeEdge.TOP, SwipeEdge.BOTTOM) &&
                (y <= mEdgeWidthOffset || y >= getMeasuredHeight() - mEdgeWidthOffset);
        }
    }

    private void onTouchMoved(int dx, int dy) {
        boolean horizontal = Math.abs(dx) > Math.abs(dy);
        View preView = getPreView();
        View topView = getTopView();
        if (horizontal) {
            swipeHorizontal(dx, preView, topView);
        } else {
            swipeVertically(dy, preView, topView);
        }
    }

    private void swipeHorizontal(int dx, View preView, View topView) {
        if (!canSwipe(SwipeEdge.LEFT, SwipeEdge.RIGHT)) return;
        if (topView == null && preView == null) {
            // TODO: 18-7-29 swipe Activity/Fragment
            if (mPuppet instanceof Fragment) {
                Fragment fragment = (Fragment) mPuppet;
                View view = fragment.getView();
                if (view == null) return;
                view.setTranslationX(dx);
            }
        } else if (topView != null && preView == null) {
            if (mIsStickyWithHost) {
                // TODO: 18-7-29 swipe Activity/Fragment
            } else {
                topView.setTranslationX(dx);
            }
        } else if (topView != null) {
            preView.setVisibility(VISIBLE);
            topView.setTranslationX(dx);
        }
    }

    private void swipeVertically(int dy, View preView, View topView) {
        if (!canSwipe(SwipeEdge.TOP, SwipeEdge.BOTTOM)) return;
        if (topView == null && preView == null) {
            // TODO: 18-7-29 swipe Activity/Fragment
        } else if (topView != null && preView == null) {
            if (mIsStickyWithHost) {
                // TODO: 18-7-29 swipe Activity/Fragment
            } else {
                topView.setTranslationY(dy);
            }
        } else if (topView != null) {
            preView.setVisibility(VISIBLE);
            topView.setTranslationY(dy);
        }
    }

    private void inertiaScroll() {

    }

    private void swipeClose() {
        boolean horizontal = Math.abs(mDX) > Math.abs(mDY);

    }

    private void swipeRestoration() {
        boolean horizontal = Math.abs(mDX) > Math.abs(mDY);
    }

    @Nullable
    private View getTopView() {
        Stack<String> stack = Rigger.getRigger(mPuppet).getFragmentStack();
        if (stack.empty()) {
            return null;
        }
        String topTag = stack.peek();
        Fragment fragment = Rigger.getRigger(mPuppet).findFragmentByTag(topTag);
        return fragment == null ? null : fragment.getView();
    }

    @Nullable
    private View getPreView() {
        Stack<String> stack = Rigger.getRigger(mPuppet).getFragmentStack();
        if (stack.size() <= 1) {
            return null;
        }
        String topTag = stack.get(stack.size() - 2);
        Fragment fragment = Rigger.getRigger(mPuppet).findFragmentByTag(topTag);
        return fragment == null ? null : fragment.getView();
    }

    /////////////////////////////Attributes setter/////////////////////////////////////

    public void setParallaxOffset(float parallaxOffset) {
        mParallaxOffset = parallaxOffset;
    }

    public void setSwipeEdgeSide(SwipeEdge[] swipeEdgeSide) {
        mSwipeEdgeSide = swipeEdgeSide;
        if (mSwipeEdgeSide == null || mSwipeEdgeSide.length == 0) {
            setEnableSwipe(false);
        } else {
            for (SwipeEdge edge : mSwipeEdgeSide) {
                if (edge == SwipeEdge.NONE) {
                    setEnableSwipe(false);
                    return;
                }
            }
            setEnableSwipe(true);
        }
        if (canSwipe(SwipeEdge.LEFT)) {
            mEdgeFlag = ViewDragHelper.EDGE_LEFT;
        }else if(canSwipe(SwipeEdge.RIGHT)){
            mEdgeFlag = ViewDragHelper.EDGE_RIGHT;
        }

    }

    public void setEdgeWidthOffset(int edgeWidthOffset) {
        final float density = getContext().getResources().getDisplayMetrics().density;
        mEdgeWidthOffset = (int) ((edgeWidthOffset * density) + 0.5f);
    }

    public void setEnableSwipe(boolean enable) {
        mIsEnable = enable;
    }

    public void setStickyWithHost(boolean stickyWithHost) {
        mIsStickyWithHost = stickyWithHost;
    }

    private boolean canSwipe(SwipeEdge... swipeEdges) {
        if (swipeEdges == null || swipeEdges.length == 0) return false;
        if (mSwipeEdgeSide == null || mSwipeEdgeSide.length == 0) {
            return false;
        } else {
            for (SwipeEdge edge : mSwipeEdgeSide) {
                for (SwipeEdge it : swipeEdges) {
                    if (edge.equals(it)) return true;
                }
            }
            return false;
        }
    }
}
