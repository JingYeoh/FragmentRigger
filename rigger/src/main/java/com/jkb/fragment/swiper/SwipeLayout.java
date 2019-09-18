package com.jkb.fragment.swiper;

import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import com.jkb.fragment.rigger.rigger.Rigger;
import com.jkb.fragment.swiper.annotation.SwipeEdge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * The widget support {@link android.app.Activity}/{@link  androidx.fragment.app.Fragment} to exit by swipe edge.
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

    private boolean mIsEnable;
    private float mParallaxOffset;
    private List<SwipeEdge> mSwipeEdgeSide;
    private boolean mStickyWithHost;
    private int mScrimMaxAlpha;
    private int mShadowWidth;

    private Object mPuppetHost;
    private ViewDragHelper mDragHelper;
    private Paint mScrimPaint;
    private Drawable[] mShadowDrawables;
    private Rect mTmpRect = new Rect();

    private static final float SCROLL_FINISH_THRESHOLD = 0.5f;
    private static final int EDGE_FLAG_NONE = -1000;
    private int mEdgeFlag = EDGE_FLAG_NONE;
    private int mCurrentSwipeOrientation;
    private float mScrollPercent;
    private float mScrimOpacity;
    private float mEdgeSize;

    private OnSwipeChangedListener mOnSwipeChangedListener;

    public SwipeLayout(@NonNull Context context) {
        this(context, null);
    }

    public SwipeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragCallback());
        final float density = context.getResources().getDisplayMetrics().density;
        mEdgeSize = (int) (20 * density + 0.5f);
    }

    public void setPuppetHost(@NonNull Object puppet) {
        mPuppetHost = puppet;
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
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            if (!mIsEnable || mEdgeFlag == EDGE_FLAG_NONE) {
                return false;
            }
            boolean dragEnable = mDragHelper.isEdgeTouched(mEdgeFlag, pointerId);
            if (dragEnable) {
                if (mDragHelper.isEdgeTouched(ViewDragHelper.EDGE_LEFT, pointerId)) {
                    mCurrentSwipeOrientation = ViewDragHelper.EDGE_LEFT;
                } else if (mDragHelper.isEdgeTouched(ViewDragHelper.EDGE_RIGHT, pointerId)) {
                    mCurrentSwipeOrientation = ViewDragHelper.EDGE_RIGHT;
                } else if (mDragHelper.isEdgeTouched(ViewDragHelper.EDGE_TOP, pointerId)) {
                    mCurrentSwipeOrientation = ViewDragHelper.EDGE_TOP;
                } else if (mDragHelper.isEdgeTouched(ViewDragHelper.EDGE_BOTTOM, pointerId)) {
                    mCurrentSwipeOrientation = ViewDragHelper.EDGE_BOTTOM;
                } else {
                    mCurrentSwipeOrientation = -1;
                }
            }
            if (mOnSwipeChangedListener != null) {
                SwipeEdge swipeEdge = null;
                if ((mCurrentSwipeOrientation & ViewDragHelper.EDGE_LEFT) != 0) {
                    swipeEdge = SwipeEdge.LEFT;
                } else if ((mCurrentSwipeOrientation & ViewDragHelper.EDGE_RIGHT) != 0) {
                    swipeEdge = SwipeEdge.RIGHT;
                } else if ((mCurrentSwipeOrientation & ViewDragHelper.EDGE_TOP) != 0) {
                    swipeEdge = SwipeEdge.TOP;
                } else if ((mCurrentSwipeOrientation & ViewDragHelper.EDGE_BOTTOM) != 0) {
                    swipeEdge = SwipeEdge.BOTTOM;
                }
                if (swipeEdge != null) {
                    mOnSwipeChangedListener.onEdgeTouched(SwipeLayout.this, swipeEdge, mPuppetHost);
                }
            }
            return dragEnable;
        }

        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
            if ((mEdgeFlag & edgeFlags) != 0) {
                mCurrentSwipeOrientation = edgeFlags;
            }
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            int ret = 0;
            if (canSwipe(SwipeEdge.LEFT) && (mCurrentSwipeOrientation & ViewDragHelper.EDGE_LEFT) != 0) {
                ret = Math.min(child.getWidth(), Math.max(left, 0));
            } else if (canSwipe(SwipeEdge.RIGHT) && (mCurrentSwipeOrientation & ViewDragHelper.EDGE_RIGHT) != 0) {
                ret = Math.min(0, Math.max(left, -child.getWidth()));
            }
            return ret;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            int ret = 0;
            if (canSwipe(SwipeEdge.TOP) && (mCurrentSwipeOrientation & ViewDragHelper.EDGE_TOP) != 0) {
                ret = Math.min(child.getHeight(), Math.max(top, 0));
            } else if (canSwipe(SwipeEdge.BOTTOM) && (mCurrentSwipeOrientation & ViewDragHelper.EDGE_BOTTOM) != 0) {
                ret = Math.min(0, Math.max(top, -child.getHeight()));
            }
            return ret;
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            if (getTopFragment() != null) {
                return 1;
            }
            if (mPuppetHost instanceof Activity) {
                return 1;
            }
            return 0;
        }

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            if (getTopFragment() != null) {
                return 1;
            }
            if (mPuppetHost instanceof Activity) {
                return 1;
            }
            return 0;
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);

            if ((mCurrentSwipeOrientation & ViewDragHelper.EDGE_LEFT) != 0) {
                mScrollPercent = Math.abs((float) left / getWidth());
            } else if ((mCurrentSwipeOrientation & ViewDragHelper.EDGE_RIGHT) != 0) {
                mScrollPercent = Math.abs((float) left / getWidth());
            } else if ((mCurrentSwipeOrientation & ViewDragHelper.EDGE_TOP) != 0) {
                mScrollPercent = Math.abs((float) top / getHeight());
            } else if ((mCurrentSwipeOrientation & ViewDragHelper.EDGE_BOTTOM) != 0) {
                mScrollPercent = Math.abs((float) top / getHeight());
            }
            invalidate();

            Fragment preFragment = getPreFragment();
            Fragment topFragment = getTopFragment();
            if (mScrollPercent == 0) {
                if (preFragment != null && preFragment.getView() != null) {
                    preFragment.getView().setVisibility(GONE);
                }
            } else if (mScrollPercent >= 1) {
                if (mOnSwipeChangedListener != null) {
                    mOnSwipeChangedListener.onSwipeBacked(SwipeLayout.this, mPuppetHost);
                }
                if (preFragment != null) {
                    View preView = preFragment.getView();
                    if (preView != null) {
                        preView.setX(0);
                        preView.setY(0);
                    }
                } else {
                    Activity activity = getPreActivity();
                    if (activity != null) {
                        View decorView = activity.getWindow().getDecorView();
                        decorView.setX(0);
                        decorView.setY(0);
                    }
                }
                if (topFragment != null) {
                    if (preFragment == null && mStickyWithHost) {
                        Rigger.getRigger(mPuppetHost).closeWithoutTransaction();
                    } else {
                        Rigger.getRigger(topFragment).closeWithoutTransaction();
                    }
                } else {
                    Rigger.getRigger(mPuppetHost).closeWithoutTransaction();
                }
            } else {
                if (mOnSwipeChangedListener != null) {
                    mOnSwipeChangedListener.onSwipeChanged(SwipeLayout.this, mPuppetHost, mScrollPercent);
                }
                if (preFragment != null) {
                    View preView = preFragment.getView();
                    if (preView != null && preView.getVisibility() != VISIBLE) {
                        preView.setVisibility(VISIBLE);
                    }
                }
            }
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            final int childWidth = releasedChild.getWidth();
            final int childHeight = releasedChild.getHeight();

            int left = 0, top = 0;
            if ((mCurrentSwipeOrientation & ViewDragHelper.EDGE_LEFT) != 0) {
                left = xvel > 0 || xvel == 0 && mScrollPercent > SCROLL_FINISH_THRESHOLD ? (childWidth) : 0;
            } else if ((mCurrentSwipeOrientation & ViewDragHelper.EDGE_RIGHT) != 0) {
                left = xvel < 0 || xvel == 0 && mScrollPercent > SCROLL_FINISH_THRESHOLD ? -(childWidth) : 0;
            } else if ((mCurrentSwipeOrientation & ViewDragHelper.EDGE_TOP) != 0) {
                top = yvel < 0 || yvel == 0 && mScrollPercent > SCROLL_FINISH_THRESHOLD ? (childHeight) : 0;
            } else if ((mCurrentSwipeOrientation & ViewDragHelper.EDGE_BOTTOM) != 0) {
                top = yvel < 0 || yvel == 0 && mScrollPercent > SCROLL_FINISH_THRESHOLD ? -(childHeight) : 0;
            }

            mDragHelper.settleCapturedViewAt(left, top);
            invalidate();
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean result = super.drawChild(canvas, child, drawingTime);
        if (child == getChildAt(0)) {
            drawShadow(canvas, child);
            drawScrim(canvas, child);
        }
        return result;
    }

    private void drawScrim(@NonNull Canvas canvas, @NonNull View child) {
        int alpha = (int) (mScrollPercent * mScrimMaxAlpha);
        mScrimPaint.setAlpha(alpha);
        canvas.drawRect(child.getLeft(), child.getTop(), child.getRight(),
                child.getBottom(), mScrimPaint);
    }

    private void drawShadow(@NonNull Canvas canvas, @NonNull View child) {
        if (mShadowDrawables == null || mShadowDrawables.length == 0) {
            return;
        }
        final Rect childRect = mTmpRect;
        child.getHitRect(childRect);

        if ((mCurrentSwipeOrientation & ViewDragHelper.EDGE_LEFT) != 0) {
            drawShadowChild(canvas, mShadowDrawables[0], childRect.left - mShadowWidth,
                    childRect.top, childRect.left, childRect.bottom);
        } else if ((mCurrentSwipeOrientation & ViewDragHelper.EDGE_RIGHT) != 0) {
            if (mShadowDrawables.length > 1) {
                drawShadowChild(canvas, mShadowDrawables[1], childRect.right,
                        childRect.top, childRect.right + mShadowWidth, childRect.bottom);
            }
        } else if ((mCurrentSwipeOrientation & ViewDragHelper.EDGE_TOP) != 0) {
            if (mShadowDrawables.length > 2) {
                drawShadowChild(canvas, mShadowDrawables[2], childRect.left,
                        childRect.top - mShadowWidth, childRect.right, childRect.top);
            }
        } else if ((mCurrentSwipeOrientation & ViewDragHelper.EDGE_BOTTOM) != 0) {
            if (mShadowDrawables.length > 3) {
                drawShadowChild(canvas, mShadowDrawables[3], childRect.left,
                        childRect.bottom, childRect.right, childRect.bottom + mShadowWidth);
            }
        }
    }

    private void drawShadowChild(Canvas canvas, Drawable drawable, int left, int top, int right, int bottom) {
        if (drawable == null) {
            return;
        }
        drawable.setBounds(left, top, right, bottom);
        drawable.setAlpha((int) (mScrimOpacity * mScrimMaxAlpha));
        drawable.draw(canvas);
    }

    @Override
    public void computeScroll() {
        mScrimOpacity = 1 - mScrollPercent;
        if (mScrimOpacity >= 0) {
            if (mDragHelper.continueSettling(true)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
            computeScrollPreView();
        }
    }

    private void computeScroll(@Nullable View view) {
        if (view == null) {
            return;
        }
        view.setX(0);
        view.setY(0);
        View capturedView = mDragHelper.getCapturedView();
        if (capturedView == null) {
            return;
        }
        int xOffset = 0;
        int yOffset = 0;
        switch (mCurrentSwipeOrientation) {
            case ViewDragHelper.EDGE_LEFT:
                xOffset = (capturedView.getLeft() - getWidth());
                break;
            case ViewDragHelper.EDGE_RIGHT:
                xOffset = (capturedView.getLeft() + getWidth());
                break;
            case ViewDragHelper.EDGE_TOP:
                yOffset = (capturedView.getTop() - getHeight());
                break;
            case ViewDragHelper.EDGE_BOTTOM:
                yOffset = (capturedView.getTop() + getHeight());
                break;
        }
        if (mParallaxOffset >= 0) {
            xOffset *= mScrimOpacity * mParallaxOffset;
            yOffset *= mScrimOpacity * mParallaxOffset;
        }
        view.setX(xOffset);
        view.setY(yOffset);
    }

    private void computeScrollPreView() {
        Fragment preFragment = getPreFragment();
        if (preFragment == null) {
            if (mPuppetHost instanceof Activity) {
                computeScrollActivityView();
            }
            return;
        }
        View view = preFragment.getView();
        computeScroll(view);
    }

    private void computeScrollActivityView() {
        Activity activity = getPreActivity();
        if (activity == null) {
            return;
        }
        View decorView = activity.getWindow().getDecorView();
        computeScroll(decorView);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                float x = getX();
                float y = getY();
                boolean disallowIntercept = false;
                if (canSwipe(SwipeEdge.LEFT) && x < getLeft() + mEdgeSize) {
                    disallowIntercept = true;
                } else if (canSwipe(SwipeEdge.RIGHT) && x < getLeft() - mEdgeSize) {
                    disallowIntercept = true;
                } else if (canSwipe(SwipeEdge.TOP) && y < getTop() + mEdgeSize) {
                    disallowIntercept = true;
                } else if (canSwipe(SwipeEdge.BOTTOM) && y < getBottom() - mEdgeSize) {
                    disallowIntercept = true;
                }
                getParent().requestDisallowInterceptTouchEvent(disallowIntercept);
                break;
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!mIsEnable || canSwipe(SwipeEdge.NONE)) return super.onInterceptTouchEvent(event);
        try {
            Fragment preFragment = getPreFragment();
            if (preFragment == null && mStickyWithHost) {
                return mDragHelper.shouldInterceptTouchEvent(event);
            }
            Fragment topFragment = getTopFragment();
            if (topFragment != null) {
                boolean ableSwipeBack = Rigger.getRigger(topFragment).isAbleSwipeBack();
                if (ableSwipeBack && topFragment.getView() != null && !topFragment.isHidden()) {
                    return false;
                }
            }
            return mDragHelper.shouldInterceptTouchEvent(event);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            mDragHelper.processTouchEvent(event);
            return true;
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return false;
    }

    @Nullable
    private Fragment getTopFragment() {
        Stack<String> stack = Rigger.getRigger(mPuppetHost).getFragmentStack();
        if (stack.empty()) {
            return null;
        }
        String topTag = stack.peek();
        return Rigger.getRigger(mPuppetHost).findFragmentByTag(topTag);
    }

    @Nullable
    private Fragment getPreFragment() {
        Stack<String> stack = Rigger.getRigger(mPuppetHost).getFragmentStack();
        if (stack.size() <= 1) {
            return null;
        }
        String topTag = stack.get(stack.size() - 2);
        return Rigger.getRigger(mPuppetHost).findFragmentByTag(topTag);
    }

    @Nullable
    private Activity getPreActivity() {
        if (mPuppetHost instanceof Activity) {
            Stack<Activity> stack = SwipeActivityManager.getInstance().getActivityStack();
            int index = stack.indexOf(mPuppetHost);
            if (index <= 0) {
                return null;
            }
            return stack.get(index - 1);
        }
        return null;
    }

    ///////////////////////////// Attributes Setter /////////////////////////////////////

    public void setParallaxOffset(float parallaxOffset) {
        mParallaxOffset = parallaxOffset;
    }

    public void setSwipeEdgeSide(SwipeEdge[] swipeEdgeSide) {
        if (swipeEdgeSide == null || swipeEdgeSide.length == 0) {
            mSwipeEdgeSide = new ArrayList<>();
            setEnableSwipe(false);
            return;
        }
        mSwipeEdgeSide = Arrays.asList(swipeEdgeSide);
        for (SwipeEdge edge : mSwipeEdgeSide) {
            if (edge == SwipeEdge.NONE) {
                if (swipeEdgeSide.length > 1) {
                    throw new IllegalArgumentException("The Swiper#edgeSide can not contain other value as" +
                            " the SwipeEdge.NONE is contained.");
                }
                setEnableSwipe(false);
                return;
            } else if (edge == SwipeEdge.ALL) {
                if (swipeEdgeSide.length > 1) {
                    throw new IllegalArgumentException("The Swiper#edgeSide can not contain other value as" +
                            " the SwipeEdge.ALL is contained.");
                }
            }
        }
        setEnableSwipe(true);

        if (canSwipe(SwipeEdge.NONE)) {
            mEdgeFlag = EDGE_FLAG_NONE;
            return;
        }
        if (canSwipe(SwipeEdge.ALL)) {
            mEdgeFlag = ViewDragHelper.EDGE_ALL;
            return;
        }
        mEdgeFlag = EDGE_FLAG_NONE;
        if (canSwipe(SwipeEdge.LEFT)) {
            if (mEdgeFlag > 0) {
                mEdgeFlag |= ViewDragHelper.EDGE_LEFT;
            } else {
                mEdgeFlag = ViewDragHelper.EDGE_LEFT;
            }
        }
        if (canSwipe(SwipeEdge.RIGHT)) {
            if (mEdgeFlag > 0) {
                mEdgeFlag |= ViewDragHelper.EDGE_RIGHT;
            } else {
                mEdgeFlag = ViewDragHelper.EDGE_RIGHT;
            }
        }
        if (canSwipe(SwipeEdge.TOP)) {
            if (mEdgeFlag > 0) {
                mEdgeFlag |= ViewDragHelper.EDGE_TOP;
            } else {
                mEdgeFlag = ViewDragHelper.EDGE_TOP;
            }
        }
        if (canSwipe(SwipeEdge.BOTTOM)) {
            if (mEdgeFlag > 0) {
                mEdgeFlag |= ViewDragHelper.EDGE_BOTTOM;
            } else {
                mEdgeFlag = ViewDragHelper.EDGE_BOTTOM;
            }
        }
    }

    public void setEnableSwipe(boolean enable) {
        mIsEnable = enable;
    }

    public void setStickyWithHost(boolean stickyWithHost) {
        mStickyWithHost = stickyWithHost;
    }

    public void setScrimColor(int scrimColor) {
        if (mScrimPaint == null) {
            mScrimPaint = new Paint();
        }
        mScrimPaint.setColor(scrimColor);
        mScrimPaint.setStyle(Style.FILL);
        invalidate();
    }

    public void setScrimMaxAlpha(int scrimMaxAlpha) {
        mScrimMaxAlpha = scrimMaxAlpha;
    }

    public void setShadowDrawable(int[] shadowDrawable) {
        if (shadowDrawable != null && shadowDrawable.length > 0) {
            if (shadowDrawable.length > 4) {
                throw new IllegalArgumentException("shadowDrawable can host only four child");
            }
            mShadowDrawables = new Drawable[shadowDrawable.length];
            for (int i = 0; i < shadowDrawable.length; i++) {
                int drawableId = shadowDrawable[i];
                if (drawableId == 0) {
                    mShadowDrawables[i] = null;
                    continue;
                }
                mShadowDrawables[i] = ContextCompat.getDrawable(getContext(), drawableId);
            }
        }
        invalidate();
    }

    public void setShadowWidth(int shadowWidth) {
        final float density = getContext().getResources().getDisplayMetrics().density;
        mShadowWidth = (int) (shadowWidth * density + 0.5f);
    }

    private boolean canSwipe(SwipeEdge... swipeEdges) {
        if (swipeEdges == null || swipeEdges.length == 0) {
            return false;
        }
        if (mSwipeEdgeSide == null || mSwipeEdgeSide.isEmpty()) {
            return false;
        }
        List<SwipeEdge> newSwipeEdges = Arrays.asList(swipeEdges);
        if (newSwipeEdges.contains(SwipeEdge.NONE)) {
            return mSwipeEdgeSide.contains(SwipeEdge.NONE);
        } else {
            if (mSwipeEdgeSide.contains(SwipeEdge.ALL)) {
                return true;
            }
        }
        for (SwipeEdge it : swipeEdges) {
            if (mSwipeEdgeSide.contains(it)) {
                return true;
            }
        }
        return false;
    }

    public void setOnSwipeChangedListener(OnSwipeChangedListener onSwipeChangedListener) {
        mOnSwipeChangedListener = onSwipeChangedListener;
    }

    /**
     * Interface definition for a callback to be invoked when the swipeLayout is swiping or
     * status is changed.
     *
     * @see #setOnSwipeChangedListener(OnSwipeChangedListener)
     */
    public interface OnSwipeChangedListener {
        /**
         * Called when the swipe edge is touched.
         *
         * @param v      The view whose swipe status has changed.
         * @param edge   The touched edge.
         * @param puppet The puppet handle this swipe.
         */
        void onEdgeTouched(SwipeLayout v, SwipeEdge edge, Object puppet);

        /**
         * Called when the swipe status of a view is changed.
         *
         * @param v       The view whose swipe status has changed.
         * @param puppet  The puppet handle this swipe.
         * @param percent The percent of swipe progress.
         */
        void onSwipeChanged(SwipeLayout v, Object puppet, float percent);

        /**
         * Called when the view is swiped back.
         *
         * @param v      The view whose swipe status has changed.
         * @param puppet The puppet handle this swipe.
         */
        void onSwipeBacked(SwipeLayout v, Object puppet);
    }
}
