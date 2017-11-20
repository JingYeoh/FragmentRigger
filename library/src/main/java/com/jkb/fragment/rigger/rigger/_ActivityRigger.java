package com.jkb.fragment.rigger.rigger;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.jkb.fragment.rigger.annotation.Puppet;
import com.jkb.fragment.rigger.model.FragmentStackManager;

/**
 * Activity Rigger.rig the Activity puppet.
 *
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 20,2017
 */

final class _ActivityRigger extends _Rigger {

  private AppCompatActivity mActivity;
  private FragmentManager mFm;
  //data
  @IdRes
  private int mContainerViewId;
  private FragmentStackManager mStackManager;
  private boolean mIsResumed = false;

  _ActivityRigger(@NonNull AppCompatActivity activity) {
    this.mActivity = activity;
    Class<? extends Activity> clazz = activity.getClass();
    Puppet puppet = clazz.getAnnotation(Puppet.class);
    mContainerViewId = puppet.containerViewId();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    mFm = mActivity.getSupportFragmentManager();
    if (savedInstanceState == null) {
      mStackManager = new FragmentStackManager();
    } else {
      mStackManager = FragmentStackManager.restoreStack(savedInstanceState);
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
    mStackManager.saveInstanceState(outState);
  }

  @Override
  public void onDestroy() {
    mStackManager = null;
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
    return null;
  }

}
