package com.yj.app.utils;

import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Dec 12,2017
 */

public class ImageViewUtil {

  /**
   * Returns the bitmap position inside an imageView.
   *
   * @param imageView source ImageView
   *
   * @return 0: left, 1: top, 2: width, 3: height
   */
  public static int[] getDisplayedImageLocation(ImageView imageView) {
    int[] ret = new int[4];

    if (imageView == null || imageView.getDrawable() == null)
      return ret;

    // Get image dimensions
    // Get image matrix values and place them in an array
    float[] f = new float[9];
    imageView.getImageMatrix().getValues(f);

    // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
    final float scaleX = f[Matrix.MSCALE_X];
    final float scaleY = f[Matrix.MSCALE_Y];

    // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
    final Drawable d = imageView.getDrawable();
    final int origW = d.getIntrinsicWidth();
    final int origH = d.getIntrinsicHeight();

    // Calculate the actual dimensions
    final int actW = Math.round(origW * scaleX);
    final int actH = Math.round(origH * scaleY);

    ret[2] = actW;
    ret[3] = actH;

    // Get image position
    // We assume that the image is centered into ImageView
    int imgViewW = imageView.getWidth();
    int imgViewH = imageView.getHeight();

    int[] imgViewScreenLoc = new int[2];
    imageView.getLocationOnScreen(imgViewScreenLoc);

    // get the actual image location inside its image view
    int left = imgViewScreenLoc[0] + (imgViewW - actW) / 2;
    int top = imgViewScreenLoc[1] + (imgViewH - actH) / 2;

    ret[0] = left;
    ret[1] = top;

    return ret;
  }
}
