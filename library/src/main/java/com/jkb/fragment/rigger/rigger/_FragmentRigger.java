package com.jkb.fragment.rigger.rigger;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

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

  private Fragment mFragment;

  _FragmentRigger(Fragment fragment) {
    this.mFragment = fragment;
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
