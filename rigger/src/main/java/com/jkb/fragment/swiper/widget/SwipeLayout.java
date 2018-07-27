package com.jkb.fragment.swiper.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import com.jkb.fragment.swiper.annotation.SwipeEdge;

/**
 * The widget support {@link android.app.Activity}/{@link android.support.v4.app.Fragment} to exit by swipe edge.
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
  private SwipeEdge[] mSwipEdgeSide;

  private Object mPuppet;

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
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    return super.onInterceptTouchEvent(ev);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return super.onTouchEvent(event);
  }

  public void setParallaxOffset(float parallaxOffset) {
    mParallaxOffset = parallaxOffset;
  }

  public void setSwipEdgeSide(SwipeEdge[] swipeEdgeSide) {
    mSwipEdgeSide = swipeEdgeSide;
    if (mSwipEdgeSide == null || mSwipEdgeSide.length == 0) {
      setEnableSwipe(false);
    } else {
      for (SwipeEdge edge : mSwipEdgeSide) {
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
}
