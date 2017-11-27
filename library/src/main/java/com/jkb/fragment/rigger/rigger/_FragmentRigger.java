package com.jkb.fragment.rigger.rigger;

import static com.jkb.fragment.rigger.utils.RiggerConsts.METHOD_GET_CONTAINERVIEWID;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import com.jkb.fragment.rigger.annotation.Puppet;
import com.jkb.fragment.rigger.exception.AlreadyExistException;
import com.jkb.fragment.rigger.exception.NotExistException;
import com.jkb.fragment.rigger.exception.RiggerException;
import com.jkb.fragment.rigger.helper.FragmentPerformer;
import com.jkb.fragment.rigger.helper.FragmentPerformer.Builder;
import com.jkb.fragment.rigger.helper.FragmentStackManager;
import com.jkb.fragment.rigger.utils.Logger;
import java.lang.reflect.Method;
import java.util.LinkedList;
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
  private static final String BUNDLE_KEY_FRAGMENT_STATUS_HIDE = "/bundle/key/fragment/status/hide";

  private Fragment mFragment;
  private Activity mActivity;
  private FragmentManager mParentFm;
  private FragmentManager mChildFm;
  //data
  @IdRes
  private int mContainerViewId;
  private boolean mBindContainerView;
  private String mFragmentTag;
  private FragmentStackManager mStackManager;
  private LinkedList<Builder> mFragmentTransactions;

  _FragmentRigger(@NonNull Fragment fragment) {
    this.mFragment = fragment;
    //init containerViewId
    Class<? extends Fragment> clazz = fragment.getClass();
    Puppet puppet = clazz.getAnnotation(Puppet.class);
    mBindContainerView = puppet.bondContainerView();
    mContainerViewId = puppet.containerViewId();
    if (mContainerViewId <= 0) {
      try {
        Method containerViewId = clazz.getMethod(METHOD_GET_CONTAINERVIEWID);
        mContainerViewId = (int) containerViewId.invoke(fragment);
      } catch (Exception ignored) {
      }
    }
    //init fragment tag
    mFragmentTag = clazz.getSimpleName() + "__" + UUID.randomUUID().toString();
    //init fragment helper
    mFragmentTransactions = new LinkedList<>();
    mStackManager = new FragmentStackManager();
  }

  @Override
  public void onAttach(Context context) {
    mActivity = (Activity) context;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    mParentFm = mFragment.getFragmentManager();
    mChildFm = mFragment.getChildFragmentManager();
    if (savedInstanceState != null) {
      mFragmentTag = savedInstanceState.getString(BUNDLE_KEY_FRAGMENT_TAG);
      mStackManager = FragmentStackManager.restoreStack(savedInstanceState);
      restoreHiddenState(savedInstanceState);
    }
  }

  /**
   * Restore the state of fragment hidden.
   */
  private void restoreHiddenState(Bundle savedInstanceState) {
    boolean isHidden = savedInstanceState.getBoolean(BUNDLE_KEY_FRAGMENT_STATUS_HIDE);
    if (isHidden) {
      commitFragmentTransaction(FragmentPerformer.beginTransaction(mParentFm).hide(mFragment));
    } else {
      commitFragmentTransaction(FragmentPerformer.beginTransaction(mParentFm).show(mFragment));
    }
  }

  @Override
  public void onResume() {
    //commit all saved fragment transaction.
    while (true) {
      Builder transaction = mFragmentTransactions.poll();
      if (transaction == null) break;
      commitFragmentTransaction(transaction);
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    outState.putString(BUNDLE_KEY_FRAGMENT_TAG, mFragmentTag);
    outState.putBoolean(BUNDLE_KEY_FRAGMENT_STATUS_HIDE, mFragment.isHidden());
    mStackManager.saveInstanceState(outState);
  }

  @Override
  public void onDestroy() {
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
    Fragment topFragment = FragmentPerformer.findFragmentByTag(mChildFm, topFragmentTag);
    if (topFragment == null) {
      throwException(new NotExistException(topFragmentTag));
    }
    Rigger.getRigger(topFragment).onRiggerBackPressed();
  }

  @Override
  public void startFragment(@NonNull Fragment fragment) {
    //if the fragment has effective containerViewId,then the operation is operated by itself.
    if (getContainerViewId() > 0) {
      String fragmentTAG = Rigger.getRigger(fragment).getFragmentTAG();
      if (!mStackManager.push(fragmentTAG, mContainerViewId)) {
        throwException(new AlreadyExistException(fragmentTAG));
      }
      commitFragmentTransaction(FragmentPerformer.beginTransaction(mChildFm)
          .add(getContainerViewId(), fragment, fragmentTAG)
          .hide(mStackManager.getFragmentTagByContainerViewId(getContainerViewId()))
          .show(fragment));
      return;
    }
    //or the operation should be operated by parent who's container view'id is effective.
    Fragment startPuppet = mFragment.getParentFragment();
    while (true) {
      if (startPuppet == null) break;
      int containerViewId = Rigger.getRigger(startPuppet).getContainerViewId();
      if (containerViewId > 0) break;
      startPuppet = startPuppet.getParentFragment();
    }
    if (startPuppet == null) {
      Rigger.getRigger(mActivity).startFragment(fragment);
    } else {
      Rigger.getRigger(startPuppet).startFragment(fragment);
    }
  }

  @Override
  public void startTopFragment() {
    String topFragmentTag = mStackManager.peek();
    if (TextUtils.isEmpty(topFragmentTag)) {
      commitFragmentTransaction(FragmentPerformer.beginTransaction(mChildFm)
          .hide(mStackManager.getFragmentTagByContainerViewId(getContainerViewId()))
      );
      return;
    }
    Fragment topFragment = FragmentPerformer.findFragmentByTag(mChildFm, topFragmentTag);
    if (topFragment == null) {
      throwException(new NotExistException(topFragmentTag));
    }
    commitFragmentTransaction(FragmentPerformer.beginTransaction(mChildFm)
        .hide(mStackManager.getFragmentTagByContainerViewId(getContainerViewId()))
        .show(topFragment));
  }

  @Override
  public void showFragment(@NonNull Fragment fragment, @IdRes int containerViewId) {
    String fragmentTAG = Rigger.getRigger(fragment).getFragmentTAG();
    Builder builder = FragmentPerformer.beginTransaction(mChildFm);
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
    commitFragmentTransaction(FragmentPerformer.beginTransaction(mChildFm)
        .remove(mStackManager.getFragmentTagByContainerViewId(containerViewId))
        .add(containerViewId, fragment, fragmentTAG)
        .show(fragment)
    );
    mStackManager.remove(containerViewId);
    mStackManager.add(fragmentTAG, containerViewId);
  }

  @Override
  public boolean isResumed() {
    return mFragment.isResumed();
  }

  @Override
  public void close() {
    mStackManager.clear();
    commitFragmentTransaction(FragmentPerformer.beginTransaction(mChildFm).clear());
    //remove this fragment from parent's stack and show the pop fragment.
    Fragment parentFragment = mFragment.getParentFragment();
    if (parentFragment != null) {
      Rigger.getRigger(parentFragment).close(mFragment);
      Rigger.getRigger(parentFragment).startTopFragment();
      return;
    }
    Rigger.getRigger(mActivity).close(mFragment);
    Rigger.getRigger(mActivity).startTopFragment();
  }

  @Override
  public void close(@NonNull Fragment fragment) {
    String fragmentTAG = Rigger.getRigger(fragment).getFragmentTAG();
    if (!mStackManager.remove(fragmentTAG)) {
      throwException(new NotExistException(fragmentTAG));
    }
    //if the stack is empty and the puppet is bond container view.then close the fragment.
    if (isBondContainerView() && mStackManager.getFragmentStack().empty()) {
      close();
    } else {
      //if the puppet is not bond container,then remove the fragment onto the container.
      //and show the Fragment's content view.
      commitFragmentTransaction(FragmentPerformer.beginTransaction(mChildFm)
          .remove(fragment));
    }
  }

  @Override
  public String getFragmentTAG() {
    return mFragmentTag;
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
      Logger.w(mFragment, "::Commit transaction---->The Activity is not resumed,the transaction will be saved");
    } else {
      transaction.commit();
    }
  }
}
