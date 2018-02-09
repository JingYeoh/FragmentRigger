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
 * Support {@link android.app.Activity}/{@link android.support.v4.app.Fragment} exit by finger swipe edge.
 *
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Feb 09,2018.
 */

public class SwiperLayout extends FrameLayout {

  //attributes
  private boolean mAbleParallax;
  private boolean mAbleShowEdgeShadow;
  private int mEdgeShadowCoor;
  private float mEdgeShadowWidth;
  private boolean mAbleEdgeShadowGradient;

  public SwiperLayout(@NonNull Context context) {
    this(context, null);
  }

  public SwiperLayout(@NonNull Context context,
      @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SwiperLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initAttributes(context, attrs);
  }

  private void initAttributes(Context context, AttributeSet attrs) {
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SwiperLayout);
    mAbleParallax = ta.getBoolean(R.styleable.SwiperLayout_parallax, true);
    mAbleShowEdgeShadow = ta.getBoolean(R.styleable.SwiperLayout_edgeShadow, true);
    mEdgeShadowCoor = ta.getColor(R.styleable.SwiperLayout_edgeShadowColor, Color.GRAY);

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
