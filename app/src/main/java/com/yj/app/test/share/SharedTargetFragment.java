package com.yj.app.test.share;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import com.jkb.fragment.rigger.rigger.Rigger;
import com.jkb.fragment.rigger.utils.Logger;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.yj.app.R;
import com.yj.app.base.BaseFragment;
import com.yj.app.utils.ImageViewUtil;

/**
 * Share target.
 *
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Dec 12,2017
 */

public class SharedTargetFragment extends BaseFragment {

  public static final int TYPE_HAMMER = 1001;
  public static final int TYPE_HOURGLASS = 1002;
  public static final int TYPE_SEND = 1003;
  public static final int TYPE_CLOUD = 1004;


  // transition properties
  private static String PROPNAME_SCREENLOCATION_LEFT = "rsspace:location:left";
  private static String PROPNAME_SCREENLOCATION_TOP = "rsspace:location:top";
  private static String PROPNAME_WIDTH = "rsspace:width";
  private static String PROPNAME_HEIGHT = "rsspace:height";
  final private Bundle mEndValues = new Bundle();

  public static SharedTargetFragment newInstance(int type) {
    Bundle args = new Bundle();
    args.putInt(BUNDLE_KEY, type);
    SharedTargetFragment fragment = new SharedTargetFragment();
    fragment.setArguments(args);
    return fragment;
  }

  private int mType;
  private ImageView imageView;
  private int[] preLocation;
  private int[] preSize;
  private Bundle mStartValues;

  @Override
  protected int getContentView() {
    return R.layout.frg_share_target;
  }

  @Override
  protected void init(Bundle savedInstanceState) {
    imageView = (ImageView) findViewById(R.id.fst_iv);
    Bundle args = savedInstanceState == null ? getArguments() : savedInstanceState;
    mType = args.getInt(BUNDLE_KEY);
    int iconId = 0;
    switch (mType) {
      case TYPE_HAMMER:
        iconId = R.drawable.hammer_h;
        break;
      case TYPE_HOURGLASS:
        iconId = R.drawable.hourglass_h;
        break;
      case TYPE_CLOUD:
        iconId = R.drawable.cloud_h;
        break;
      case TYPE_SEND:
        iconId = R.drawable.send_h;
        break;
    }
    //start transaction anim.
    preLocation = args.getIntArray(BUNDLE_KEY + 1);
    preSize = args.getIntArray(BUNDLE_KEY + 2);
    mStartValues = new Bundle();
    mStartValues.putInt(PROPNAME_WIDTH, preSize[0]);
    mStartValues.putInt(PROPNAME_HEIGHT, preSize[1]);
    mStartValues.putInt(PROPNAME_SCREENLOCATION_LEFT, preLocation[0]);
    mStartValues.putInt(PROPNAME_SCREENLOCATION_TOP, preLocation[1]);

    Picasso.with(mContext).load(iconId).into(imageView, new Callback() {
      @Override
      public void onSuccess() {
        onUiReady();
      }

      @Override
      public void onError() {
      }
    });
  }

  private void onUiReady() {
    imageView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
      @Override
      public boolean onPreDraw() {
        imageView.getViewTreeObserver().removeOnPreDrawListener(this);
        prepareScene();
        runEnterAnimation();
        return true;
      }
    });
  }

  private void prepareScene() {
    captureScaleValues(mEndValues, imageView);
    // calculate the scale factors
    float scaleX = scaleDelta(mStartValues, mEndValues, PROPNAME_WIDTH);
    float scaleY = scaleDelta(mStartValues, mEndValues, PROPNAME_HEIGHT);

    // scale the image
    imageView.setScaleX(scaleX);
    imageView.setScaleY(scaleY);

    // as scaling the image will change the top and left coordinates, we need to re-capture
    // the values to proper figure out the translation deltas w.r.t. to start view
    captureScreenLocationValues(mEndValues, imageView);

    int deltaX = translationDelta(mStartValues, mEndValues, PROPNAME_SCREENLOCATION_LEFT);
    int deltaY = translationDelta(mStartValues, mEndValues, PROPNAME_SCREENLOCATION_TOP);
    // finally, translate the end view to where the start view was
    imageView.setTranslationX(deltaX);
    imageView.setTranslationY(deltaY);
  }

  /**
   * This method will run the entry animation
   */
  private void runEnterAnimation() {
    // We can now make it visible
    imageView.setVisibility(View.VISIBLE);
    // finally, run the animation
    if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH) {
      imageView.animate()
          .setDuration(300)
          .setInterpolator(new AccelerateDecelerateInterpolator())
          .scaleX(1f)
          .scaleY(1f)
          .translationX(0)
          .translationY(0)
          .start();
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(BUNDLE_KEY, mType);
  }

  private float scaleDelta(
      @NonNull Bundle startValues,
      @NonNull Bundle endValues,
      @NonNull String propertyName) {

    int startValue = startValues.getInt(propertyName);
    int endValue = endValues.getInt(propertyName);
    float delta = (float) startValue / endValue;

    return delta;
  }

  private int translationDelta(
      @NonNull Bundle startValues,
      @NonNull Bundle endValues,
      @NonNull String propertyName) {

    int startValue = startValues.getInt(propertyName);
    int endValue = endValues.getInt(propertyName);
    int delta = startValue - endValue;

    return delta;
  }

  /**
   * Helper method to capture the view values to animate
   *
   * @param view target view
   *
   * @return Bundle with the captured values
   */
  private static Bundle captureValues(@NonNull View view) {
    Bundle b = new Bundle();
    captureScaleValues(b, view);
    captureScreenLocationValues(b, view);
    return b;
  }

  private static void captureScaleValues(@NonNull Bundle b, @NonNull View view) {
    if (view instanceof ImageView) {
      int[] size = ImageViewUtil.getDisplayedImageLocation((ImageView) view);
      b.putInt(PROPNAME_WIDTH, size[2]);
      b.putInt(PROPNAME_HEIGHT, size[3]);
    } else {
      b.putInt(PROPNAME_WIDTH, view.getWidth());
      b.putInt(PROPNAME_HEIGHT, view.getHeight());
    }
  }

  private static void captureScreenLocationValues(@NonNull Bundle b, @NonNull View view) {
    if (view instanceof ImageView) {
      int[] size = ImageViewUtil.getDisplayedImageLocation((ImageView) view);
      b.putInt(PROPNAME_SCREENLOCATION_LEFT, size[0]);
      b.putInt(PROPNAME_SCREENLOCATION_TOP, size[1]);
    } else {
      int[] screenLocation = new int[2];
      view.getLocationOnScreen(screenLocation);
      b.putInt(PROPNAME_SCREENLOCATION_LEFT, screenLocation[0]);
      b.putInt(PROPNAME_SCREENLOCATION_TOP, screenLocation[1]);
    }
  }

  /**
   * This method will run the entry animation
   */
  private void runExitAnimation() {
    // re-calculate deltas
    int deltaX = translationDelta(mStartValues, mEndValues, PROPNAME_SCREENLOCATION_LEFT);
    int deltaY = translationDelta(mStartValues, mEndValues, PROPNAME_SCREENLOCATION_TOP);
    float scaleX = scaleDelta(mStartValues, mEndValues, PROPNAME_WIDTH);
    float scaleY = scaleDelta(mStartValues, mEndValues, PROPNAME_HEIGHT);

    if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
      imageView.animate()
          .setDuration(300)
          .setInterpolator(new AccelerateDecelerateInterpolator())
          .scaleX(scaleX)
          .scaleY(scaleY)
          .translationX(deltaX)
          .translationY(deltaY)
          .withEndAction(new Runnable() {
            @Override
            public void run() {
              Rigger.getRigger(SharedTargetFragment.this).close();
            }
          }).start();
    }
  }

  public void onRiggerBackPressed() {
    Logger.d(this, "onBackPressed");
    runExitAnimation();
  }
}
