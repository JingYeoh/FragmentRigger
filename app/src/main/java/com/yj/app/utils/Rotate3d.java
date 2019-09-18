package com.yj.app.utils;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import androidx.annotation.NonNull;

/**
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Dec 13,2017
 */

public class Rotate3d extends Animation {

  private View view;

  public Rotate3d(@NonNull View view) {
    this.view = view;
    setDuration(1000);
  }

  @Override
  protected void applyTransformation(float interpolatedTime, Transformation t) {
    Camera camera = new Camera();
    camera.save();

    // 设置camera动作为绕Y轴旋转
    // 总共旋转180度，因此计算在每个补间时间点interpolatedTime的角度即为两着相乘
//    camera.rotateX(deg * interpolatedTime);
    camera.rotateY(180 * interpolatedTime);
//    camera.rotateZ(180 * interpolatedTime);
//
    // 根据camera动作产生一个matrix，赋给Transformation的matrix，以用来设置动画效果
    Matrix matrix = t.getMatrix();
    camera.getMatrix(matrix);

    camera.restore();
    //经过以下平移，才能以view的中心点进行翻转
    matrix.preTranslate(-view.getWidth() / 2, -view.getHeight() / 2);
    matrix.postTranslate(view.getWidth() / 2, view.getHeight() / 2);
  }
}
