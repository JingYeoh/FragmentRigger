package com.jkb.fragment.rigger.rigger;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

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

  _ActivityRigger(AppCompatActivity activity) {
    this.mActivity = activity;
  }

  @Override
  public void startFragment(@NonNull Fragment fragment) {

  }

  @Override
  public boolean isResumed() {
    return false;
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

  @Override
  public void onCreate(Bundle savedInstanceState) {

  }

  @Override
  public void onResumeFragments() {

  }

  @Override
  public void onResume() {

  }

  @Override
  public void onPause() {

  }

  @Override
  public void onSaveInstanceState(Bundle outState) {

  }

  @Override
  public void onDestroy() {

  }
}
