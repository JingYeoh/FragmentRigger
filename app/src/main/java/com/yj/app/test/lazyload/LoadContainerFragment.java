package com.yj.app.test.lazyload;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import com.jkb.fragment.rigger.annotation.LazyLoad;
import com.jkb.fragment.rigger.annotation.Puppet;
import com.jkb.fragment.rigger.rigger.Rigger;
import com.yj.app.R;
import com.yj.app.base.BaseFragment;
import com.yj.app.test.start.StartFragment;

/**
 * Demo of lazy load container.
 *
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Dec 13,2017
 */
@Puppet(containerViewId = R.id.lazyContent)
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
      if (mValue.equals("Me")) {
//        findViewById(R.id.flc_tv).setVisibility(View.GONE);
        findViewById(R.id.lazyContent).setVisibility(View.VISIBLE);
      } else {
        findViewById(R.id.flc_tv).setVisibility(View.VISIBLE);
        findViewById(R.id.lazyContent).setVisibility(View.GONE);
      }
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
    Rigger.getRigger(LoadContainerFragment.this).startFragment(StartFragment.newInstance(0));
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putString(BUNDLE_KEY, mValue);
  }

  public boolean onRiggerBackPressed() {
    if (!mValue.equals("Me")) return true;
    if (Rigger.getRigger(this).getFragmentStack().size() == 0) return false;
    return true;
  }

  public boolean onInterruptBackPressed() {
    if (mValue.equals("Me")) {
      if (Rigger.getRigger(this).getFragmentStack().size() == 0) return true;
      return false;
    }
    return true;
  }
}
