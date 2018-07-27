package com.jkb.fragment.swiper;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * The bridge to connect Rigger lib and Swiper lib.
 *
 * @author JingYeoh
 * <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 * <a href="https://github.com/justkiddingbaby">Github</a>
 * <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since 2018-07-27
 */
public interface SwiperDelegate {

  /**
   * Returns the host {@link Activity}.
   *
   * @return the host Activity.
   */
  @NonNull
  Activity getHostActivity();

  /**
   * Allow or not exit the host {@link Activity} or {@link Fragment} as the rigger stack size is 1.
   *
   * @return Returns true , the host will be closed as the rigger stack size is 1 or empty .
   * Otherwise the host will be closed only as the stack is empty.
   */
  boolean stickyWithActivity();

  /**
   * Returns the prev Fragment in the rigger stack.
   *
   * @return the prev fragment or null
   */
  @Nullable
  Fragment getPrevFragment();
}
