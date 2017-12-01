package com.jkb.fragment.rigger.rigger;

import static com.jkb.fragment.rigger.utils.RiggerConsts.METHOD_GET_CONTAINERVIEWID;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import com.jkb.fragment.rigger.annotation.Puppet;
import com.jkb.fragment.rigger.exception.AlreadyExistException;
import com.jkb.fragment.rigger.exception.NotExistException;
import com.jkb.fragment.rigger.exception.RiggerException;
import com.jkb.fragment.rigger.exception.UnSupportException;
import com.jkb.fragment.rigger.helper.FragmentStackManager;
import java.lang.reflect.Method;

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
  @IdRes
  private int mContainerViewId;
  private boolean mBindContainerView;
  private RiggerTransaction mRiggerTransaction;
  private FragmentStackManager mStackManager;
  private boolean mIsResumed = false;

  _ActivityRigger(@NonNull AppCompatActivity activity) {
    this.mActivity = activity;
    //init annotation and container
    Class<? extends Activity> clazz = activity.getClass();
    Puppet puppet = clazz.getAnnotation(Puppet.class);
    mBindContainerView = puppet.bondContainerView();
    mContainerViewId = puppet.containerViewId();
    if (mContainerViewId <= 0) {
      try {
        Method containerViewId = clazz.getMethod(METHOD_GET_CONTAINERVIEWID);
        mContainerViewId = (int) containerViewId.invoke(activity);
      } catch (Exception ignored) {
      }
    }
    //init fragment helper
    mStackManager = new FragmentStackManager();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    if (mRiggerTransaction == null) {
      mRiggerTransaction = new RiggerTransactionImpl(this, mActivity.getSupportFragmentManager());
    }
    if (savedInstanceState != null) {
      mStackManager = FragmentStackManager.restoreStack(savedInstanceState);
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
  public void onRiggerBackPressed() {
    String topFragmentTag = mStackManager.peek();
    //the stack is empty,close the Activity.
    if (TextUtils.isEmpty(topFragmentTag)) {
      close();
      return;
    }
    //call the top fragment's onRiggerBackPressed method.
    Fragment topFragment = mRiggerTransaction.find(topFragmentTag);
    if (topFragment == null) {
      throwException(new NotExistException(topFragmentTag));
    }
    Rigger.getRigger(topFragment).onRiggerBackPressed();
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
  public void startFragmentForResult(Object receive, @NonNull Fragment fragment, int requestCode) {
    Bundle arguments = fragment.getArguments();
    if (arguments == null) arguments = new Bundle();
    Message message = Message.obtain();
    message.obj = receive;
    message.arg1 = requestCode;
    arguments.putParcelable(BUNDLE_KEY_FOR_RESULT, message);
    fragment.setArguments(arguments);
    startFragment(fragment);
  }

  @Override
  public void startFragmentForResult(@NonNull Fragment fragment, int requestCode) {
    startFragmentForResult(null, fragment, requestCode);
  }

  @Override
  public void startTopFragment() {
    String topFragmentTag = mStackManager.peek();
    mRiggerTransaction.hide(mStackManager.getFragmentTags(getContainerViewId()));
    if (!TextUtils.isEmpty(topFragmentTag)) {
      mRiggerTransaction.show(topFragmentTag);
    }
    mRiggerTransaction.commit();
  }

  @Override
  public void showFragment(@NonNull Fragment fragment, @IdRes int containerViewId) {
    String fragmentTAG = Rigger.getRigger(fragment).getFragmentTAG();
    if (mStackManager.add(fragmentTAG, containerViewId)) {
      mRiggerTransaction.add(containerViewId, fragment, fragmentTAG);
    }
    mRiggerTransaction.hide(mStackManager.getFragmentTags(containerViewId))
        .show(fragmentTAG)
        .commit();
  }

  @Override
  public void showFragment(@NonNull String tag) {
    int containerViewId = mStackManager.getContainer(tag);
    if (containerViewId == 0) {
      throwException(new NotExistException(tag));
    }
    showFragment(mRiggerTransaction.find(tag), containerViewId);
  }

  @Override
  public void hideFragment(@NonNull Fragment fragment) {
    String fragmentTAG = Rigger.getRigger(fragment).getFragmentTAG();
    mRiggerTransaction.hide(fragmentTAG)
        .commit();
  }

  @Override
  public void hideFragment(@NonNull String tag) {
    if (!mStackManager.contain(tag)) {
      throwException(new NotExistException(tag));
    }
    hideFragment(mRiggerTransaction.find(tag));
  }

  @Override
  public void replaceFragment(@NonNull Fragment fragment, @IdRes int containerViewId) {
    String fragmentTAG = Rigger.getRigger(fragment).getFragmentTAG();
    mRiggerTransaction.add(containerViewId, fragment, fragmentTAG)
        .remove(mStackManager.getFragmentTags(containerViewId))
        .show(fragmentTAG)
        .commit();
    mStackManager.remove(containerViewId);
    mStackManager.add(fragmentTAG, containerViewId);
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
  public void close(@NonNull Fragment fragment) {
    String fragmentTAG = Rigger.getRigger(fragment).getFragmentTAG();
    if (!mStackManager.remove(fragmentTAG)) {
      throwException(new NotExistException(fragmentTAG));
    }
    //if the stack is empty and the puppet is bond container view.then finish the activity.
    if (isBondContainerView() && mStackManager.getFragmentStack().empty()) {
      close();
    } else {
      //if the puppet is not bond container,then remove the fragment onto the container.
      //and show the Activity's content view.
      mRiggerTransaction.remove(fragmentTAG)
          .commit();
    }
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

  /**
   * Throw the exception.
   */
  private void throwException(RiggerException e) {
    throw e;
  }
}
