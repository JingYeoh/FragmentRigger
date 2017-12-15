package com.yj.app.utils;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * An animation that rotates the view on the Y axis between two specified angles.
 * This animation also adds a translation on the Z axis (depth) to improve the effect.
 * <p>
 * <a href="https://android.googlesource.com/platform/development/+/master/samples/ApiDemos/src/com/example/android/apis/animation/Rotate3dAnimation.java">Rotate3dAnimation</a>
 */
public class Rotate3dAnimation extends Animation {

  private static final int TYPE_SCALE = 0;
  private static final int TYPE_PX = 1;

  private final float mFromDegrees;
  private final float mToDegrees;
  private float mCenterX;
  private float mCenterY;
  private float mDepthZ;
  private int mType = TYPE_PX;
  private final boolean mReverse;
  private Camera mCamera;

  /**
   * Creates a new 3D rotation on the Y axis. The rotation is defined by its
   * start angle and its end angle. Both angles are in degrees. The rotation
   * is performed around a center point on the 2D space, definied by a pair
   * of X and Y coordinates, called centerX and centerY. When the animation
   * starts, a translation on the Z axis (depth) is performed. The length
   * of the translation can be specified, as well as whether the translation
   * should be reversed in time.
   *
   * @param fromDegrees the start angle of the 3D rotation
   * @param toDegrees   the end angle of the 3D rotation
   * @param centerX     the X center of the 3D rotation
   * @param centerY     the Y center of the 3D rotation
   * @param reverse     true if the translation should be reversed, false otherwise
   */
  public Rotate3dAnimation(float fromDegrees, float toDegrees,
      float centerX, float centerY, float depthZ, boolean reverse) {
    mFromDegrees = fromDegrees;
    mToDegrees = toDegrees;
    mCenterX = centerX;
    mCenterY = centerY;
    mDepthZ = depthZ;
    mReverse = reverse;
  }

  public Rotate3dAnimation(float fromDegrees, float toDegrees
      , float centerX, float centerY, float depthZ
      , boolean reverse, int type) {
    mFromDegrees = fromDegrees;
    mToDegrees = toDegrees;
    mCenterX = centerX;
    mCenterY = centerY;
    mDepthZ = depthZ;
    mReverse = reverse;
    mType = type;
  }

  public Rotate3dAnimation(float fromDegrees, float toDegrees, boolean reverse) {
    this(fromDegrees, toDegrees, 0.5f, 0.5f, 0.5f, reverse, TYPE_SCALE);
  }

  @Override
  public void initialize(int width, int height, int parentWidth, int parentHeight) {
    super.initialize(width, height, parentWidth, parentHeight);
    mCamera = new Camera();
    if (mType == TYPE_SCALE) {
      mCenterX = width * mCenterX;
      mCenterY = height * mCenterY;
      mDepthZ = width * mDepthZ;
    }
  }

  @Override
  protected void applyTransformation(float interpolatedTime, Transformation t) {
    final float fromDegrees = mFromDegrees;
    float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);

    final float centerX = mCenterX;
    final float centerY = mCenterY;
    final Camera camera = mCamera;
    final Matrix matrix = t.getMatrix();
    camera.save();
    if (mReverse) {
      camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
    } else {
      camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
    }
    camera.rotateY(degrees);
    camera.getMatrix(matrix);
    camera.restore();

    matrix.preTranslate(-centerX, -centerY);
    matrix.postTranslate(centerX, centerY);
  }
}
