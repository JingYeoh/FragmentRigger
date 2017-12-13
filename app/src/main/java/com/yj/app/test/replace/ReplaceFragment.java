package com.yj.app.test.replace;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.jkb.fragment.rigger.rigger.Rigger;
import com.yj.app.R;
import com.yj.app.base.BaseFragment;
import com.yj.app.test.ContainerFragment;
import com.yj.app.utils.Rotate3d;
import java.util.UUID;

/**
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 30,2017
 */

public class ReplaceFragment extends BaseFragment implements OnClickListener {

  public static ReplaceFragment newInstance() {
    Bundle args = new Bundle();
    ReplaceFragment fragment = new ReplaceFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  protected int getContentView() {
    return R.layout.frg_replace;
  }

  @Override
  protected void init(Bundle savedInstanceState) {
    if (savedInstanceState == null) {
      ContainerFragment fragment = ContainerFragment.newInstance(UUID.randomUUID().toString());
      Rigger.getRigger(this).replaceFragment(fragment, R.id.fr_content);
    }
    initListener();
  }

  private void initListener() {
    findViewById(R.id.fr_bt).setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.fr_bt:
//        Rigger.getRigger(this)
//            .replaceFragment(ContainerFragment.newInstance(UUID.randomUUID().toString()), R.id.fr_content);
        Rotate3d rotate3d = new Rotate3d(v);
        rotate3d.setDuration(1000);
        v.startAnimation(rotate3d);
        break;
    }
  }
}
