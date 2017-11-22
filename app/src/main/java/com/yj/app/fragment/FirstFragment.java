package com.yj.app.fragment;

import android.os.Bundle;
import android.widget.TextView;
import com.jkb.fragment.rigger.annotation.Puppet;
import com.yj.app.R;
import com.yj.app.base.BaseFragment;

/**
 * TODO: Please input the description of this class.
 *
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 21,2017
 */
@Puppet
public class FirstFragment extends BaseFragment {

  private static int count = 0;

  public static FirstFragment newInstance() {
    count++;
    Bundle args = new Bundle();
    FirstFragment fragment = new FirstFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  protected int getRootView() {
    return R.layout.frg_test;
  }

  @Override
  protected void init(Bundle savedInstanceState) {
    ((TextView) findViewById(R.id.ft_tv)).setText("StartFragment：" + count);
  }
}
