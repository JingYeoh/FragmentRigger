package com.jkb.fragment.rigger.rigger;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import com.jkb.fragment.rigger.exception.UnSupportException;
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
  private RiggerTransaction mParentRiggerTransaction;
  private String mFragmentTag;

  private Message mForResultTarget;

  _FragmentRigger(@NonNull Fragment fragment) {
    super(fragment);
    this.mFragment = fragment;
    //init fragment tag
    mFragmentTag = fragment.getClass().getSimpleName() + "__" + UUID.randomUUID().toString().substring(0, 8);
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
      restoreHiddenState(savedInstanceState);
    }
  }

  /**
   * Init the params of {@link #startFragmentForResult(Fragment, int)}
   */
  private void initResultParams(Bundle savedInstanceState) {
    Bundle bundle = savedInstanceState == null ? mFragment.getArguments() : savedInstanceState;
    mForResultTarget = bundle.getParcelable(BUNDLE_KEY_FOR_RESULT);
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
    outState.putParcelable(BUNDLE_KEY_FOR_RESULT, mForResultTarget);
    mStackManager.saveInstanceState(outState);
  }

  @Override
  public void onDestroy() {
  }

  @Override
  public void startFragment(@NonNull Fragment fragment) {
    //if the fragment has effective containerViewId,then the operation is operated by itself.
    if (getContainerViewId() > 0) {
      super.startFragment(fragment);
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
  public String getFragmentTAG() {
    return mFragmentTag;
  }

  @Override
  public void setResult(int resultCode, Bundle bundle) {
    if (mForResultTarget == null) {
      throwException(new UnSupportException("class " + this + " is not started by startFragmentForResult() method"));
    }
    //get the host object.
    Object host = mForResultTarget.obj;
    if (host == null) {
      Fragment startPuppet = mFragment.getParentFragment();
      while (true) {
        if (startPuppet == null) break;
        int containerViewId = Rigger.getRigger(startPuppet).getContainerViewId();
        if (containerViewId > 0) break;
        startPuppet = startPuppet.getParentFragment();
      }
      host = startPuppet == null ? mActivity : startPuppet;
    }
    //invoke the host#onFragmentResult method.
    Class<?> clazz = host.getClass();
    try {
      Method method = clazz.getMethod(RiggerConsts.METHOD_ONFRAGMENTRESULT, int.class, int.class, Bundle.class);
      method.invoke(host, mForResultTarget.arg1, resultCode, bundle);
    } catch (NoSuchMethodException ignored) {
      Logger.w(this, "Not found method " + RiggerConsts.METHOD_ONFRAGMENTRESULT + " in class " + clazz.getSimpleName());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
