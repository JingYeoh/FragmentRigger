package com.jkb.fragment.rigger.rigger;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import com.jkb.fragment.rigger.exception.AlreadyExistException;
import com.jkb.fragment.rigger.exception.UnSupportException;

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
  //data
  private boolean mIsResumed = false;

  _ActivityRigger(@NonNull AppCompatActivity activity) {
    super(activity);
    this.mActivity = activity;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (mRiggerTransaction == null) {
      mRiggerTransaction = new RiggerTransactionImpl(this, mActivity.getSupportFragmentManager());
    }
  }

  @Override
  public void onResumeFragments() {
    mIsResumed = true;
    mRiggerTransaction.commit();
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
    if (mActivity.isFinishing()) {
      mStackManager.clear();
      mRiggerTransaction.removeAll();
    }
  }

  @Override
  public void startFragment(@NonNull Fragment fragment) {
    String fragmentTAG = Rigger.getRigger(fragment).getFragmentTAG();
    if (!mStackManager.push(fragmentTAG, mContainerViewId)) {
      throwException(new AlreadyExistException(fragmentTAG));
    }
    if (getContainerViewId() <= 0) {
      throwException(new UnSupportException("ContainerViewId must be effective in class " + mActivity.getClass()));
    }
    mRiggerTransaction.add(mContainerViewId, fragment, fragmentTAG)
        .hide(mStackManager.getFragmentTags(getContainerViewId()))
        .show(fragmentTAG)
        .commit();
  }

  @Override
  public boolean isResumed() {
    return mIsResumed;
  }

  @Override
  public void close() {
    mActivity.finish();
  }

  @Override
  public String getFragmentTAG() {
    throwException(new UnSupportException("getFragmentTAG() method can only be called by Fragment"));
    return null;
  }

  @Override
  public int getContainerViewId() {
    return mContainerViewId;
  }

  @Override
  public boolean isBondContainerView() {
    return mBindContainerView;
  }

  @Override
  public void setResult(int resultCode, Bundle bundle) {
    throwException(new UnSupportException("setResult() method can only be called by Fragment"));
  }
}
