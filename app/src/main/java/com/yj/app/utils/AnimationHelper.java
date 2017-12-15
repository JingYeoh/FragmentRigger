package com.yj.app.utils;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

/**
 * AnimationHelper
 *
 * @author xl
 * @version 1.0
 * @since 06/09/2017
 */
public class AnimationHelper {

  private AnimationHelper() {
  }

  public static Animation createRotate3dExitAnimation() {
    Rotate3dAnimation animation = new Rotate3dAnimation(0, 90, true);
    animation.setDuration(300);
    animation.setFillAfter(false);
    animation.setInterpolator(new AccelerateInterpolator());
    return animation;
  }

  public static Animation createRotate3dEnterAnimation() {
    final Rotate3dAnimation animation = new Rotate3dAnimation(270, 360, false);
    animation.setDuration(600);
    animation.setStartOffset(300);
    animation.setFillAfter(false);
    animation.setInterpolator(new DecelerateInterpolator());
    return animation;
  }
}