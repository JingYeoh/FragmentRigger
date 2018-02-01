package com.jkb.fragment.rigger.rigger;

import static com.jkb.fragment.rigger.utils.RiggerConsts.METHOD_GET_FRAGMENT_TAG;
import static com.jkb.fragment.rigger.utils.RiggerConsts.METHOD_GET_PUPPET_ANIMATIONS;
import static com.jkb.fragment.rigger.utils.RiggerConsts.METHOD_ON_LAZYLOAD_VIEW_CREATED;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.jkb.fragment.rigger.annotation.Animator;
import com.jkb.fragment.rigger.annotation.LazyLoad;
import com.jkb.fragment.rigger.exception.UnSupportException;
import com.jkb.fragment.rigger.utils.Logger;
import com.jkb.fragment.rigger.utils.RiggerConsts;
import java.lang.reflect.InvocationTargetException;
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
  private static final String BUNDLE_KEY_FRAGMENT_LAZYLOAD_ABLE = "/bundle/key/fragment/lazyLoad/able";
  private static final String BUNDLE_KEY_FRAGMENT_LAZYLOAD_INVOKE = "/bundle/key/fragment/lazyLoad/invoke";
  private static final String BUNDLE_KEY_FRAGMENT_VIEW_INIT = "/bundle/key/fragment/view/init";
  private static final String BUNDLE_KEY_FRAGMENT_ANIMATION = "/bundle/key/fragment/animation";

  private Fragment mFragment;
  private Activity mActivity;
  //data
  private RiggerTransaction mParentRiggerTransaction;
  private String mFragmentTag;
  private Bundle mSavedFragmentState;
  //anim
  int mEnterAnim;
  int mExitAnim;
  int mPopEnterAnim;
  int mPopExitAnim;
  //lazy load
  private boolean mAbleLazyLoad = false;
  private boolean mHasInitView = false;
  private boolean mHasInvokeLazyLoad = false;

  private Message mForResultTarget;

  _FragmentRigger(@NonNull Fragment fragment) {
    super(fragment);
    this.mFragment = fragment;
    //init lazy load.
    Class<? extends Fragment> clazz = mFragment.getClass();
    LazyLoad lazyLoad = clazz.getAnnotation(LazyLoad.class);
    if (lazyLoad != null) {
      mAbleLazyLoad = lazyLoad.value();
    }
    //init fragment animator
    invokeFragmentAnimators(clazz);
    //init fragment tag
    invokeCustomFragmentTag(clazz);
  }

  /**
   * invoke class to get custom fragment tag.
   */
  @SuppressWarnings("All")
  private void invokeCustomFragmentTag(Class<? extends Fragment> clazz) {
    try {
      Method method = clazz.getMethod(METHOD_GET_FRAGMENT_TAG);
      Object value = method.invoke(mFragment);
      if (!(value instanceof String)) {
        throwException(new UnSupportException("Method " + METHOD_GET_FRAGMENT_TAG + " return value must be String"));
      }
      mFragmentTag = (String) value;
    } catch (NoSuchMethodException ignore) {
      mFragmentTag = mFragment.getClass().getSimpleName() + "__" + UUID.randomUUID().toString().substring(0, 8);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  /**
   * Init fragment animators.
   */
  @SuppressWarnings("All")
  private void invokeFragmentAnimators(Class<? extends Fragment> clazz) {
    Animator animator = clazz.getAnnotation(Animator.class);
    if (animator != null) {
      mEnterAnim = animator.enter();
      mExitAnim = animator.exit();
      mPopEnterAnim = animator.popEnter();
      mPopExitAnim = animator.popExit();
    }
    try {
      Method method = clazz.getMethod(METHOD_GET_PUPPET_ANIMATIONS);
      Object values = method.invoke(mFragment);
      if (values == null) {
        throwException(
            new UnSupportException("Method " + METHOD_GET_PUPPET_ANIMATIONS + " return value can't be null"));
      }
      if (!(values instanceof int[])) {
        throwException(
            new UnSupportException("Method " + METHOD_GET_PUPPET_ANIMATIONS + " return value's type must be int[]"));
      }
      int[] animators = (int[]) values;
      if (animators == null || animators.length != 4) {
        throwException(
            new UnSupportException("Method " + METHOD_GET_PUPPET_ANIMATIONS + " return value's length must be 4"));
      }
      mEnterAnim = animators[0];
      mExitAnim = animators[1];
      mPopEnterAnim = animators[2];
      mPopExitAnim = animators[3];
    } catch (NoSuchMethodException ignore) {
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns the container host.
   */
  private Object getContainerHost() {
    Fragment parent = mFragment.getParentFragment();
    while (true) {
      if (parent == null) break;
      int containerViewId = Rigger.getRigger(parent).getContainerViewId();
      if (containerViewId > 0) break;
      parent = parent.getParentFragment();
    }
    if (parent == null) {
      return mActivity;
    }
    return parent;
  }

  @Override
  public void onAttach(Context context) {
    mActivity = (Activity) context;
    mContext = context;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mSavedFragmentState = savedInstanceState;
    //init rigger transaction
    if (mRiggerTransaction == null) {
      mRiggerTransaction = new RiggerTransactionImpl(Rigger.getRigger(mActivity), mFragment.getChildFragmentManager());
    }
    if (mParentRiggerTransaction == null) {
      mParentRiggerTransaction = new RiggerTransactionImpl(Rigger.getRigger(mActivity), mFragment.getFragmentManager());
    }
    //init params of startForResult
    initResultParams(savedInstanceState);
    //restore attributes
    if (savedInstanceState != null) {
      mAbleLazyLoad = savedInstanceState.getBoolean(BUNDLE_KEY_FRAGMENT_LAZYLOAD_ABLE);
      mHasInitView = savedInstanceState.getBoolean(BUNDLE_KEY_FRAGMENT_VIEW_INIT);
      mHasInvokeLazyLoad = savedInstanceState.getBoolean(BUNDLE_KEY_FRAGMENT_LAZYLOAD_INVOKE);
      mFragmentTag = savedInstanceState.getString(BUNDLE_KEY_FRAGMENT_TAG);
      mEnterAnim = savedInstanceState.getInt(BUNDLE_KEY_FRAGMENT_ANIMATION + 1, 0);
      mExitAnim = savedInstanceState.getInt(BUNDLE_KEY_FRAGMENT_ANIMATION + 2, 0);
      mPopEnterAnim = savedInstanceState.getInt(BUNDLE_KEY_FRAGMENT_ANIMATION + 3, 0);
      mPopExitAnim = savedInstanceState.getInt(BUNDLE_KEY_FRAGMENT_ANIMATION + 4, 0);
      restoreHiddenState(savedInstanceState);
    }
  }

  @Override
  View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mHasInitView = true;
    initLazyLoadStatus();
    return null;
  }

  @Override
  void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mHasInitView = true;
    mSavedFragmentState = savedInstanceState;
    invokeOnLazyLoadViewCreated();
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
    outState.putInt(BUNDLE_KEY_FRAGMENT_ANIMATION + 1, mEnterAnim);
    outState.putInt(BUNDLE_KEY_FRAGMENT_ANIMATION + 2, mExitAnim);
    outState.putInt(BUNDLE_KEY_FRAGMENT_ANIMATION + 3, mPopEnterAnim);
    outState.putInt(BUNDLE_KEY_FRAGMENT_ANIMATION + 4, mPopExitAnim);
    outState.putBoolean(BUNDLE_KEY_FRAGMENT_STATUS_HIDE, mFragment.isHidden());
    outState.putBoolean(BUNDLE_KEY_FRAGMENT_LAZYLOAD_ABLE, mAbleLazyLoad);
    outState.putBoolean(BUNDLE_KEY_FRAGMENT_LAZYLOAD_INVOKE, mHasInvokeLazyLoad);
    outState.putBoolean(BUNDLE_KEY_FRAGMENT_VIEW_INIT, mHasInitView);
    outState.putParcelable(BUNDLE_KEY_FOR_RESULT, mForResultTarget);
    mStackManager.saveInstanceState(outState);
  }

  @Override
  public void onDestroy() {
    initLazyLoadStatus();
  }

  @Override
  public void onDetach() {

  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    invokeOnLazyLoadViewCreated();
  }

  @Override
  public void startFragment(@NonNull Fragment fragment) {
    //if the fragment has effective containerViewId,then the operation is operated by itself.
    if (getContainerViewId() > 0) {
      super.startFragment(fragment);
      return;
    }
    //or the operation should be operated by parent who's container view'id is effective.
    Rigger.getRigger(getContainerHost()).startFragment(fragment);
  }

  @Override
  public boolean isResumed() {
    return mFragment.isResumed();
  }

  @Override
  public void close() {
    //start the exiting animation.
    if (mExitAnim != 0 && !mFragment.isHidden()) {
      boolean isParentBond = Rigger.getRigger(getContainerHost()).isBondContainerView();
      int parentStackSize = Rigger.getRigger(getContainerHost()).getFragmentStack().size();
      //the exiting animation will not execute when the host's mBindContainerView is true and hots's stack size is one.
      if (!isParentBond || parentStackSize > 0) {
        Animation animation = AnimationUtils.loadAnimation(mActivity, mExitAnim);
        if (animation != null) {
          setHWLayerAnimListenerIfAlpha(mFragment.getView(), animation);
          mFragment.getView().startAnimation(animation);
        }
      }
    }
    mStackManager.clear();
    mRiggerTransaction.removeAll();
    Rigger.getRigger(getContainerHost()).close(mFragment);
    Animation animation = null;
    if (mPopEnterAnim > 0) {
      animation = AnimationUtils.loadAnimation(mContext, mPopEnterAnim);
    }
    ((_Rigger) Rigger.getRigger(getContainerHost())).startPopFragment(animation);
  }

  @Override
  public void setFragmentTag(@NonNull String tag) {
    mFragmentTag = tag;
  }

  @Override
  public String getFragmentTAG() {
    return mFragmentTag;
  }

  @Override
  public boolean isLazyLoading() {
    return mAbleLazyLoad;
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
      Method method = clazz.getMethod(RiggerConsts.METHOD_ON_FRAGMENT_RESULT, int.class, int.class, Bundle.class);
      method.invoke(host, mForResultTarget.arg1, resultCode, bundle);
    } catch (NoSuchMethodException ignored) {
      Logger
          .w(this, "Not found method " + RiggerConsts.METHOD_ON_FRAGMENT_RESULT + " in class " + clazz.getSimpleName());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Init lazy load status.
   * When the method {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} ia called.this fragment must be rebuild
   * a new instance.
   */
  private void initLazyLoadStatus() {
    mHasInitView = false;
    mHasInvokeLazyLoad = false;
  }

  /**
   * Invoke method onLazyLoadViewCreated.Rotate3d
   */
  private void invokeOnLazyLoadViewCreated() {
    //make sure the method onLazyViewCreated will be called after onViewCreated.
    if (!mAbleLazyLoad || !mHasInitView) return;
    if (!mFragment.getUserVisibleHint()) return;
    //make sure the method onLazyViewCreated will be called only once.
    if (mHasInvokeLazyLoad) return;

    Method onLazyLoadViewCreated = null;
    try {
      onLazyLoadViewCreated = mFragment.getClass()
          .getMethod(METHOD_ON_LAZYLOAD_VIEW_CREATED, Bundle.class);
    } catch (NoSuchMethodException e) {
      Logger.e(mFragment, "can not find method " + METHOD_ON_LAZYLOAD_VIEW_CREATED);
    }
    if (onLazyLoadViewCreated == null) {
      throwException(new UnSupportException("can not find method " + METHOD_ON_LAZYLOAD_VIEW_CREATED));
    }
    try {
      onLazyLoadViewCreated.invoke(mFragment, mSavedFragmentState);
      mHasInvokeLazyLoad = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
