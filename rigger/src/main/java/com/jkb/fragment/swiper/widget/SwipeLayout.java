package com.jkb.fragment.swiper.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.jkb.fragment.swiper.annotation.SwipeEdge;

/**
 * The widget support {@link android.app.Activity}/{@link android.support.v4.app.Fragment} to exit by swipe edge.
 *
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

  private Object mPuppet;
  private int mEdgeWidthOffset;

  private int mLastX;
  private int mLastY;
  private int mDX;
  private int mDY;

  public SwipeLayout(@NonNull Context context) {
    this(context, null);
  }

  public SwipeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SwipeLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
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

  @Override
  public boolean onInterceptTouchEvent(MotionEvent event) {
    boolean result = super.onInterceptTouchEvent(event);
    if (!mIsEnable || canSwipe(SwipeEdge.NONE)) return result;
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        break;
      case MotionEvent.ACTION_MOVE:
        break;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        break;
    }
    return true;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    boolean result = super.onTouchEvent(event);
    if (!mIsEnable || canSwipe(SwipeEdge.NONE)) return result;
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        mLastX = (int) getX();
        mLastY = (int) getY();
        break;
      case MotionEvent.ACTION_MOVE:
        float currentX = event.getX();
        float currentY = event.getY();
        int dx = (int) (mLastX - currentX);
        int dy = (int) (mLastY - currentY);
        onTouchMoved(dx, dy);
        mLastX = (int) currentX;
        mLastY = (int) currentY;
        mDX = dx;
        mDY = dy;
        break;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        inertiaScroll();
        mDX = 0;
        mDY = 0;
        break;
    }
    return true;
  }

  private void onTouchMoved(int dx, int dy) {

  }

  private void inertiaScroll() {

  }

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
  }

  public void setEnableSwipe(boolean enable) {
    mIsEnable = enable;
  }

  private boolean canSwipe(SwipeEdge swipeEdge) {
    if (mSwipeEdgeSide == null || mSwipeEdgeSide.length == 0) {
      return false;
    } else {
      for (SwipeEdge edge : mSwipeEdgeSide) {
        if (edge == swipeEdge) return true;
      }
      return false;
    }
  }
}
