package com.yj.app.test;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.animation.Animation;
import android.widget.ImageView;
import com.jkb.fragment.rigger.annotation.LazyLoad;
import com.jkb.fragment.rigger.utils.Logger;
import com.yj.app.R;
import com.yj.app.base.BaseFragment;
import com.yj.app.utils.Rotate3d;

/**
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 30,2017
 */
@LazyLoad
public class ContainerFragment extends BaseFragment {

  public static ContainerFragment newInstance(int value) {
    Bundle args = new Bundle();
    args.putInt(BUNDLE_KEY, value);
    ContainerFragment fragment = new ContainerFragment();
    fragment.setArguments(args);
    return fragment;
  }

  private int position;
  private int[] icons = new int[]{
      R.drawable.heart, R.drawable.block, R.drawable.motorcycle, R.drawable.bear, R.drawable.content_cloud
  };
  private int[] colors = new int[]{
      R.color.bg_heart, R.color.bg_block, R.color.bg_motorcycle, R.color.bg_bear, R.color.bg_cloud
  };

  @Override
  protected int getContentView() {
    return R.layout.frg_content;
  }

  @Override
  protected void init(Bundle savedInstanceState) {
    Logger.d(this, "init isUserHintVisible=" + getUserVisibleHint());
    Bundle args = savedInstanceState == null ? getArguments() : savedInstanceState;
    position = args.getInt(BUNDLE_KEY);

  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(BUNDLE_KEY, position);
  }

  public void onLazyLoadViewCreated(Bundle savedInstanceState) {
    Logger.d(this, "onLazyLoadViewCreated()");
    ((ImageView) findViewById(R.id.fc_iv)).setImageResource(icons[position % icons.length]);
    findViewById(R.id.fc_content)
        .setBackgroundColor(ContextCompat.getColor(mContext, colors[position % icons.length]));
  }

  public int[] getPuppetAnimRes() {
    return new int[]{
        R.anim.push_left_in_no_alpha, R.anim.push_right_out_no_alpha,
        R.anim.push_right_in_no_alpha, R.anim.push_left_out_no_alpha
    };
  }

  public Animation[] getPuppetAnimations() {
    return new Animation[]{
        new Rotate3d(getView()), null, null, null
    };
  }
}
