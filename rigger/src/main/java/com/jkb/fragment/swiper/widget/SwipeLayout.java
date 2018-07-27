package com.jkb.fragment.swiper.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import com.jkb.fragment.rigger.R;

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

  //attributes
  private boolean mAbleParallax;
  private boolean mAbleShowEdgeShadow;
  private int mEdgeShadowCoor;
  private float mEdgeShadowWidth;
  private boolean mAbleEdgeShadowGradient;

  public SwipeLayout(@NonNull Context context) {
    this(context, null);
  }

  public SwipeLayout(@NonNull Context context,
      @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SwipeLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initAttributes(context, attrs);
  }

  private void initAttributes(Context context, AttributeSet attrs) {
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SwipeLayout);
    mAbleParallax = ta.getBoolean(R.styleable.SwipeLayout_parallax, true);
    mAbleShowEdgeShadow = ta.getBoolean(R.styleable.SwipeLayout_edgeShadow, true);
    mEdgeShadowCoor = ta.getColor(R.styleable.SwipeLayout_edgeShadowColor, Color.GRAY);

    ta.recycle();
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    return super.onInterceptTouchEvent(ev);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return super.onTouchEvent(event);
  }
}
