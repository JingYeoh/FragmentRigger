package com.yj.app.test;

import android.os.Bundle;
import android.widget.TextView;
import com.jkb.fragment.rigger.annotation.LazyLoad;
import com.jkb.fragment.rigger.utils.Logger;
import com.yj.app.R;
import com.yj.app.base.BaseFragment;

/**
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 30,2017
 */
@LazyLoad
public class ContainerFragment extends BaseFragment {

  public static ContainerFragment newInstance(String value) {
    Bundle args = new Bundle();
    args.putString(BUNDLE_KEY, value);
    ContainerFragment fragment = new ContainerFragment();
    fragment.setArguments(args);
    return fragment;
  }

  private String value;

  @Override
  protected int getContentView() {
    return R.layout.frg_content;
  }

  @Override
  protected void init(Bundle savedInstanceState) {
    Bundle args = savedInstanceState == null ? getArguments() : savedInstanceState;
    value = args.getString(BUNDLE_KEY);
    ((TextView) findViewById(R.id.fc_tv)).setText(value);

    Logger.d(this, "isUserHintVisible=" + getUserVisibleHint());
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putString(BUNDLE_KEY, value);
  }

  public void onLazyLoadViewCreated(Bundle savedInstanceState) {
    Logger.d(this, "onLazyLoadViewCreated()");
    Logger.d(this, "isUserHintVisible=" + getUserVisibleHint());
  }
}
