package com.yj.app;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.jkb.fragment.rigger.rigger.Rigger;
import com.yj.app.base.BaseFragment;
import com.yj.app.test.StartFragment;

/**
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 23,2017
 */

public class TestFragment extends BaseFragment implements OnClickListener {

  public static TestFragment newInstance() {
    Bundle args = new Bundle();
    TestFragment fragment = new TestFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  protected int getContentView() {
    return R.layout.frg_test;
  }

  @Override
  protected void init(Bundle savedInstanceState) {
    findViewById(R.id.fs_startFragment).setOnClickListener(this);
    findViewById(R.id.fs_showFragment).setOnClickListener(this);
    findViewById(R.id.fs_replaceFragment).setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.fs_startFragment:
        Rigger.getRigger(this).startFragment(StartFragment.newInstance(0));
        Rigger.getRigger(this).close();
        break;
      case R.id.fs_showFragment:
        break;
      case R.id.fs_replaceFragment:
        break;
    }
  }
}
