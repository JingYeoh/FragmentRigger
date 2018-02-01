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

  private ArrayList<String> mContainerTags;
  private ArrayList<String> mFragmentTags;
  private int[] mContainer = new int[]{
      R.id.fs_content1, R.id.fs_content2, R.id.fs_content3, R.id.fs_content4
  };

  @Override
  protected int getContentView() {
    return R.layout.frg_show;
  }

  @Override
  protected void init(Bundle savedInstanceState) {
    if (savedInstanceState == null) {
      mFragmentTags = new ArrayList<>();
      mContainerTags = new ArrayList<>();
      Fragment[][] fragments = new Fragment[4][2];
      for (int i = 0; i < 4; i++) {
        fragments[i][0] = ContainerFragment.newInstance(i, i % 2 == 0);
        fragments[i][1] = ContainerFragment.newInstance(4, i % 2 == 0);
        mFragmentTags.add(Rigger.getRigger(fragments[i][0]).getFragmentTAG());
        mContainerTags.add(Rigger.getRigger(fragments[i][1]).getFragmentTAG());
        Rigger.getRigger(this).addFragment(mContainer[i], fragments[i][0]);
        Rigger.getRigger(this).addFragment(mContainer[i], fragments[i][1]);
      }
      for (int i = 0; i < 4; i++) {
        Rigger.getRigger(this).showFragment(mFragmentTags.get(i));
      }
    } else {
      mFragmentTags = savedInstanceState.getStringArrayList(BUNDLE_KEY);
      mContainerTags = savedInstanceState.getStringArrayList(BUNDLE_KEY + 1);
    }
    initListener();
  }

  private void initListener() {
    findViewById(R.id.fs_content1).setOnClickListener(this);
    findViewById(R.id.fs_content2).setOnClickListener(this);
    findViewById(R.id.fs_content3).setOnClickListener(this);
    findViewById(R.id.fs_content4).setOnClickListener(this);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putStringArrayList(BUNDLE_KEY, mFragmentTags);
    outState.putStringArrayList(BUNDLE_KEY + 1, mContainerTags);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.fs_content1:
        showFragment(0);
        break;
      case R.id.fs_content2:
        showFragment(1);
        break;
      case R.id.fs_content3:
        showFragment(2);
        break;
      case R.id.fs_content4:
        showFragment(3);
        break;
    }
  }

  private void showFragment(int position) {
    Fragment fragment = Rigger.getRigger(this).findFragmentByTag(mFragmentTags.get(position));
    Rigger.getRigger(this).showFragment(mFragmentTags.get(position), false);
    if (position == 2) return;
    if (fragment.isHidden()) {
      Rigger.getRigger(this).showFragment(mFragmentTags.get(position));
    } else {
      Rigger.getRigger(this).showFragment(mContainerTags.get(position));
    }
  }
}
