package com.jkb.fragment.rigger.rigger;

import static com.jkb.fragment.rigger.utils.RiggerConsts.METHOD_GET_CONTAINERVIEWID;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import com.jkb.fragment.rigger.annotation.Puppet;
import com.jkb.fragment.rigger.exception.AlreadyExistException;
import com.jkb.fragment.rigger.exception.NotExistException;
import com.jkb.fragment.rigger.exception.RiggerException;
import com.jkb.fragment.rigger.exception.UnSupportException;
import com.jkb.fragment.rigger.helper.FragmentPerformer;
import com.jkb.fragment.rigger.helper.FragmentPerformer.Builder;
import com.jkb.fragment.rigger.helper.FragmentStackManager;
import com.jkb.fragment.rigger.utils.Logger;
import java.lang.reflect.Method;
import java.util.LinkedList;

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
  private boolean mBindContainerView;
  private FragmentStackManager mStackManager;
  private LinkedList<Builder> mFragmentTransactions;
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
    mFragmentTransactions = new LinkedList<>();
    mStackManager = new FragmentStackManager();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    mFm = mActivity.getSupportFragmentManager();
    if (savedInstanceState != null) {
      mStackManager = FragmentStackManager.restoreStack(savedInstanceState);
    }
  }

  @Override
  public void onResumeFragments() {
    mIsResumed = true;
    //commit all saved fragment transaction.
    while (true) {
      Builder transaction = mFragmentTransactions.poll();
      if (transaction == null) break;
      commitFragmentTransaction(transaction);
    }
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
      mFragmentTransactions.clear();
      commitFragmentTransaction(FragmentPerformer.beginTransaction(mFm).clear());
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
    Fragment topFragment = FragmentPerformer.findFragmentByTag(mFm, topFragmentTag);
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
    commitFragmentTransaction(FragmentPerformer.beginTransaction(mFm)
        .add(getContainerViewId(), fragment, fragmentTAG)
        .hide(mStackManager.getFragmentTagByContainerViewId(getContainerViewId()))
        .show(fragment));
  }

  @Override
  public void startTopFragment() {
    String topFragmentTag = mStackManager.peek();
    if (TextUtils.isEmpty(topFragmentTag)) {
      commitFragmentTransaction(FragmentPerformer.beginTransaction(mFm)
          .hide(mStackManager.getFragmentTagByContainerViewId(getContainerViewId()))
      );
      return;
    }
    Fragment topFragment = FragmentPerformer.findFragmentByTag(mFm, topFragmentTag);
    if (topFragment == null) {
      throwException(new NotExistException(topFragmentTag));
    }
    commitFragmentTransaction(FragmentPerformer.beginTransaction(mFm)
        .hide(mStackManager.getFragmentTagByContainerViewId(getContainerViewId()))
        .show(topFragment));
  }

  @Override
  public void showFragment(@NonNull Fragment fragment, @IdRes int containerViewId) {
    String fragmentTAG = Rigger.getRigger(fragment).getFragmentTAG();
    Builder builder = FragmentPerformer.beginTransaction(mFm);
    if (!mStackManager.add(fragmentTAG, containerViewId)) {
      builder.add(containerViewId, fragment, fragmentTAG);
    }
    commitFragmentTransaction(builder
        .hide(mStackManager.getFragmentTagByContainerViewId(containerViewId))
        .show(fragment)
    );
  }

  @Override
  public void replaceFragment(@NonNull Fragment fragment, @IdRes int containerViewId) {
    String fragmentTAG = Rigger.getRigger(fragment).getFragmentTAG();
    commitFragmentTransaction(FragmentPerformer.beginTransaction(mFm)
        .remove(mStackManager.getFragmentTagByContainerViewId(containerViewId))
        .add(containerViewId, fragment, fragmentTAG)
        .show(fragment)
    );
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
      commitFragmentTransaction(FragmentPerformer.beginTransaction(mFm)
          .remove(fragment));
    }
  }

  @Override
  public String getFragmentTAG() {
     /*This method is called by Fragment,here is useless*/
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

  /**
   * Throw the exception.
   */
  private void throwException(RiggerException e) {
    throw e;
  }

  /**
   * Commit fragment transaction.if the Activity is not resumed,then this transaction will be saved and commit as the
   * Activity is resumed.
   */
  private void commitFragmentTransaction(@NonNull Builder transaction) {
    if (!isResumed()) {
      mFragmentTransactions.add(transaction);
      Logger.w(mActivity, "::Commit transaction---->The Activity is not resumed,the transaction will be saved");
    } else {
      transaction.commit();
    }
  }
}
