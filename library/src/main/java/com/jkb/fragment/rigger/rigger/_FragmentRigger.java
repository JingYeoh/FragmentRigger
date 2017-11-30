package com.jkb.fragment.rigger.rigger;

import static com.jkb.fragment.rigger.utils.RiggerConsts.METHOD_GET_CONTAINERVIEWID;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import com.jkb.fragment.rigger.annotation.Puppet;
import com.jkb.fragment.rigger.exception.AlreadyExistException;
import com.jkb.fragment.rigger.exception.NotExistException;
import com.jkb.fragment.rigger.exception.RiggerException;
import com.jkb.fragment.rigger.helper.FragmentStackManager;
import com.jkb.fragment.rigger.utils.Logger;
import com.jkb.fragment.rigger.utils.RiggerConsts;
import java.lang.reflect.Method;
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
  //data
  @IdRes
  private int mContainerViewId;
  private boolean mBindContainerView;
  private RiggerTransaction mRiggerTransaction;
  private RiggerTransaction mParentRiggerTransaction;
  private String mFragmentTag;
  private FragmentStackManager mStackManager;

  private int mRequestCode = -1;
  private boolean mIsForResult = false;

  private _FragmentRigger(@NonNull Fragment fragment) {
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
    mStackManager = new FragmentStackManager();
  }

  /**
   * Returns the host object of fragment.
   */
  private Object getHost() {
    Fragment parent = mFragment.getParentFragment();
    return parent == null ? mActivity : parent;
  }

  @Override
  public void onAttach(Context context) {
    mActivity = (Activity) context;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    //init rigger transaction
    if (mRiggerTransaction == null) {
      mRiggerTransaction = new RiggerTransactionImpl(this, mFragment.getChildFragmentManager());
    }
    if (mParentRiggerTransaction == null) {
      mParentRiggerTransaction = new RiggerTransactionImpl(this, mFragment.getFragmentManager());
    }
    //init params of startForResult
    initResultParams(savedInstanceState);
    //restore attributes
    if (savedInstanceState != null) {
      mFragmentTag = savedInstanceState.getString(BUNDLE_KEY_FRAGMENT_TAG);
      mStackManager = FragmentStackManager.restoreStack(savedInstanceState);
      restoreHiddenState(savedInstanceState);
    }
  }

  /**
   * Init the params of {@link #startFragmentForResult(Fragment, int)}
   */
  private void initResultParams(Bundle savedInstanceState) {
    Bundle bundle = savedInstanceState == null ? mFragment.getArguments() : savedInstanceState;
    mRequestCode = bundle.getInt(BUNDLE_KEY_REQUEST_CODE);
    mIsForResult = bundle.getBoolean(BUNDLE_KEY_FOR_RESULT);
  }

  /**
   * Restore the state of fragment hidden.
   */
  private void restoreHiddenState(Bundle savedInstanceState) {
    boolean isHidden = savedInstanceState.getBoolean(BUNDLE_KEY_FRAGMENT_STATUS_HIDE);
    if (isHidden) {
      mParentRiggerTransaction.hide(getFragmentTAG()).commit();
    } else {
      mParentRiggerTransaction.show(getFragmentTAG()).commit();
    }
  }

  @Override
  public void onResume() {
    //commit all saved fragment transaction.
    mParentRiggerTransaction.commit();
    mRiggerTransaction.commit();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    outState.putString(BUNDLE_KEY_FRAGMENT_TAG, mFragmentTag);
    outState.putBoolean(BUNDLE_KEY_FRAGMENT_STATUS_HIDE, mFragment.isHidden());
    outState.putBoolean(BUNDLE_KEY_FOR_RESULT, mIsForResult);
    outState.putInt(BUNDLE_KEY_REQUEST_CODE, mRequestCode);
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
    Fragment topFragment = mRiggerTransaction.find(topFragmentTag);
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
      mRiggerTransaction.add(mContainerViewId, fragment, fragmentTAG)
          .hide(mStackManager.getFragmentTags(getContainerViewId()))
          .show(fragmentTAG)
          .commit();
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
  public void startFragmentForResult(@NonNull Fragment fragment, int requestCode) {
    Bundle arguments = fragment.getArguments();
    if (arguments == null) arguments = new Bundle();
    arguments.putBoolean(BUNDLE_KEY_FOR_RESULT, true);
    arguments.putInt(BUNDLE_KEY_REQUEST_CODE, requestCode);
    fragment.setArguments(arguments);
    startFragment(fragment);
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
    return mFragment.isResumed();
  }

  @Override
  public void close() {
    mStackManager.clear();
    mRiggerTransaction.removeAll();
    //remove this fragment from parent's stack and show the pop fragment.
    Fragment parentFragment = mFragment.getParentFragment();
    if (parentFragment != null) {
      Rigger.getRigger(parentFragment).close(mFragment);
      Rigger.getRigger(parentFragment).startTopFragment();
      return;
    }
    Rigger.getRigger(getHost()).close(mFragment);
    Rigger.getRigger(getHost()).startTopFragment();
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
      mRiggerTransaction.remove(fragmentTAG)
          .commit();
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

  @Override
  public void setResult(int resultCode, Bundle bundle) {
    //get the host object.
    Fragment startPuppet = mFragment.getParentFragment();
    while (true) {
      if (startPuppet == null) break;
      int containerViewId = Rigger.getRigger(startPuppet).getContainerViewId();
      if (containerViewId > 0) break;
      startPuppet = startPuppet.getParentFragment();
    }
    Object host = startPuppet == null ? mActivity : startPuppet;
    //invoke the host#onFragmentResult method.
    Class<?> clazz = host.getClass();
    try {
      Method method = clazz.getMethod(RiggerConsts.METHOD_ONFRAGMENTRESULT, Integer.class, Integer.class, Bundle.class);
      method.invoke(host, mRequestCode, resultCode, bundle);
    } catch (NoSuchMethodException ignored) {
      Logger.w(this, "Not found method " + RiggerConsts.METHOD_ONFRAGMENTRESULT + " in class " + clazz.getSimpleName());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Throw the exception.
   */
  private void throwException(RiggerException e) {
    throw e;
  }
}
