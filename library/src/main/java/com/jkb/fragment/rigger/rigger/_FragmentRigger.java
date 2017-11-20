package com.jkb.fragment.rigger.rigger;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.jkb.fragment.rigger.annotation.Puppet;
import java.util.UUID;

/**
 * Fragment Rigger.rig the Fragment puppet.
 *
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 20,2017
 */

final class _FragmentRigger extends _Rigger {

  private static final String BUNDLE_KEY_FRAGMENT_TAG = "/bundle/key/fragment/tag";

  private Fragment mFragment;
  private FragmentManager mFm;
  private FragmentManager mChildFm;
  //data
  @IdRes
  private int mContainerViewId;
  private String mFragmentTag;
  private boolean mIsResumed = false;


  _FragmentRigger(@NonNull Fragment fragment) {
    this.mFragment = fragment;
    //init containerViewId
    Class<? extends Fragment> clazz = fragment.getClass();
    Puppet puppet = clazz.getAnnotation(Puppet.class);
    mContainerViewId = puppet.containerViewId();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    mFm = mFragment.getFragmentManager();
    if (savedInstanceState == null) {
      mFragmentTag = UUID.randomUUID().toString();
    } else {
      mFragmentTag = savedInstanceState.getString(BUNDLE_KEY_FRAGMENT_TAG);
    }
  }

  @Override
  public void onResumeFragments() {
    mIsResumed = true;
  }

  @Override
  public void onResume() {
    mIsResumed = true;
  }

  @Override
  public void onPause() {
    mIsResumed = false;
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    outState.putString(BUNDLE_KEY_FRAGMENT_TAG, mFragmentTag);
  }

  @Override
  public void onDestroy() {

  }

  @Override
  public void startFragment(@NonNull Fragment fragment) {

  }

  @Override
  public boolean isResumed() {
    return mIsResumed;
  }

  @Override
  public void finish() {

  }

  @Override
  public void finish(@NonNull Fragment fragment) {

  }

  @Override
  public String getFragmentTAG() {
    return mFragmentTag;
  }
}
