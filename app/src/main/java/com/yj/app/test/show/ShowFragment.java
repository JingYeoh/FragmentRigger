package com.yj.app.test.show;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import com.jkb.fragment.rigger.rigger.Rigger;
import com.yj.app.R;
import com.yj.app.base.BaseFragment;
import com.yj.app.test.ContainerFragment;
import java.util.ArrayList;

/**
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 30,2017
 */

public class ShowFragment extends BaseFragment implements OnClickListener {

  public static ShowFragment newInstance() {
    Bundle args = new Bundle();
    ShowFragment fragment = new ShowFragment();
    fragment.setArguments(args);
    return fragment;
  }

  private ArrayList<String> mFragmentTags;

  @Override
  protected int getContentView() {
    return R.layout.frg_menu;
  }

  @Override
  protected void init(Bundle savedInstanceState) {
    if (savedInstanceState == null) {
      mFragmentTags = new ArrayList<>();
      Fragment[] fragments = new Fragment[4];
      for (int i = 0; i < 4; i++) {
        fragments[i] = ContainerFragment.newInstance(i + "");
        mFragmentTags.add(Rigger.getRigger(fragments[i]).getFragmentTAG());
      }
      Rigger.getRigger(this).addFragment(R.id.fs_content, fragments);
//      Rigger.getRigger(this).showFragment(mFragmentTags.get(0));
    } else {
      mFragmentTags = savedInstanceState.getStringArrayList(BUNDLE_KEY);
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
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putStringArrayList(BUNDLE_KEY, mFragmentTags);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.fs_bt_1:
        Rigger.getRigger(this).showFragment(mFragmentTags.get(0));
        break;
      case R.id.fs_bt_2:
        Rigger.getRigger(this).showFragment(mFragmentTags.get(1));
        break;
      case R.id.fs_bt_3:
        Rigger.getRigger(this).showFragment(mFragmentTags.get(2));
        break;
      case R.id.fs_bt_4:
        Rigger.getRigger(this).showFragment(mFragmentTags.get(3));
        break;
    }
  }
}
