package com.yj.app.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.jkb.fragment.rigger.annotation.Puppet;
import com.jkb.fragment.rigger.rigger.Rigger;
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
@Puppet(containerViewId = R.id.firstContent)
public class FirstFragment extends BaseFragment implements OnClickListener {

  public static FirstFragment newInstance(int count) {
    Bundle args = new Bundle();
    args.putInt(BUNDLE_KEY, count);
    FirstFragment fragment = new FirstFragment();
    fragment.setArguments(args);
    return fragment;
  }

  //data
  private int mCount;

  @Override
  protected int getContentView() {
    return R.layout.frg_first;
  }

  @Override
  protected void init(Bundle savedInstanceState) {
    Bundle args = savedInstanceState == null ? getArguments() : savedInstanceState;
    mCount = args.getInt(BUNDLE_KEY);
    ((TextView) findViewById(R.id.ft_tv)).setText("FirstFragment：" + mCount);

    findViewById(R.id.fs_startFragment).setOnClickListener(this);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(BUNDLE_KEY, mCount);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.fs_startFragment:
        Rigger.getRigger(this).startFragment(FirstFragment.newInstance(mCount + 1));
        break;
    }
  }
}
