package com.yj.app.test.share;

import static com.yj.app.test.share.SharedTargetFragment.TYPE_CLOUD;
import static com.yj.app.test.share.SharedTargetFragment.TYPE_HAMMER;
import static com.yj.app.test.share.SharedTargetFragment.TYPE_HOURGLASS;
import static com.yj.app.test.share.SharedTargetFragment.TYPE_SEND;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.jkb.fragment.rigger.rigger.Rigger;
import com.yj.app.R;
import com.yj.app.base.BaseFragment;

/**
 * Demo of shared element for fragment.
 *
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Dec 12,2017
 */

public class SharedElementFragment extends BaseFragment implements OnClickListener {

  public static SharedElementFragment newInstance() {
    Bundle args = new Bundle();
    SharedElementFragment fragment = new SharedElementFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  protected int getContentView() {
    return R.layout.frg_share_index;
  }

  @Override
  protected void init(Bundle savedInstanceState) {
    findViewById(R.id.fsi_hammer).setOnClickListener(this);
    findViewById(R.id.fsi_hourglass).setOnClickListener(this);
    findViewById(R.id.fsi_send).setOnClickListener(this);
    findViewById(R.id.fsi_cloud).setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.fsi_hammer:
        startSecondFragment(TYPE_HAMMER, v);
        break;
      case R.id.fsi_hourglass:
        startSecondFragment(TYPE_HOURGLASS, v);
        break;
      case R.id.fsi_send:
        startSecondFragment(TYPE_SEND, v);
        break;
      case R.id.fsi_cloud:
        startSecondFragment(TYPE_CLOUD, v);
        break;
    }
  }

  private void startSecondFragment(int type, View view) {
    SharedTargetFragment fragment = SharedTargetFragment.newInstance(type);
    Bundle bundle = fragment.getArguments();
    int[] screenLocation = new int[2];
    view.getLocationOnScreen(screenLocation);
    bundle.putIntArray(BUNDLE_KEY + 1, screenLocation);
    bundle.putIntArray(BUNDLE_KEY + 2, new int[]{view.getWidth(), view.getHeight()});
    switch (type) {
      case TYPE_HAMMER:
        break;
      case TYPE_HOURGLASS:
        break;
      case TYPE_SEND:
        break;
      case TYPE_CLOUD:
        break;
    }
    Rigger.getRigger(this).startFragment(fragment);
  }
}
