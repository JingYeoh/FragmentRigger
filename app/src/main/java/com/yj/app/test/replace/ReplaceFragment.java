package com.yj.app.test.replace;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.jkb.fragment.rigger.rigger.Rigger;
import com.yj.app.R;
import com.yj.app.base.BaseFragment;
import com.yj.app.test.ContentFragment;
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
    return R.layout.frg_menu;
  }

  @Override
  protected void init(Bundle savedInstanceState) {
    if (savedInstanceState == null) {
      ContentFragment fragment = ContentFragment.newInstance(UUID.randomUUID().toString());
      Rigger.getRigger(this).replaceFragment(fragment, R.id.fs_content);
    }
    initListener();
  }

  private void initListener() {
    findViewById(R.id.fs_bt_1).setOnClickListener(this);
    findViewById(R.id.fs_bt_2).setOnClickListener(this);
    findViewById(R.id.fs_bt_3).setOnClickListener(this);
    findViewById(R.id.fs_bt_4).setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.fs_bt_1:
      case R.id.fs_bt_2:
      case R.id.fs_bt_3:
      case R.id.fs_bt_4:
        Rigger.getRigger(this)
            .replaceFragment(ContentFragment.newInstance(UUID.randomUUID().toString()), R.id.fs_content);
        break;
    }
  }
}
