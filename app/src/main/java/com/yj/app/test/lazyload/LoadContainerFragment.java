package com.yj.app.test.lazyload;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import com.jkb.fragment.rigger.annotation.LazyLoad;
import com.jkb.fragment.rigger.utils.Logger;
import com.yj.app.R;
import com.yj.app.base.BaseFragment;

/**
 * Demo of lazy load container.
 *
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Dec 13,2017
 */
@LazyLoad
public class LoadContainerFragment extends BaseFragment {

  public static LoadContainerFragment newInstance(String value) {
    Bundle args = new Bundle();
    args.putString(BUNDLE_KEY, value);
    LoadContainerFragment fragment = new LoadContainerFragment();
    fragment.setArguments(args);
    return fragment;
  }

  private String mValue;
  private Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      findViewById(R.id.flc_load).setVisibility(View.GONE);
      findViewById(R.id.flc_tv).setVisibility(View.VISIBLE);
    }
  };

  @Override
  protected int getContentView() {
    return R.layout.frg_lazy_container;
  }

  @Override
  protected void init(Bundle savedInstanceState) {
    Bundle args = savedInstanceState == null ? getArguments() : savedInstanceState;
    mValue = args.getString(BUNDLE_KEY);
  }

  public void onLazyLoadViewCreated(Bundle savedInstanceState) {
    ((TextView) findViewById(R.id.flc_tv)).setText(mValue);
    mHandler.sendEmptyMessageDelayed(1001, 1000);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putString(BUNDLE_KEY, mValue);
  }
}
