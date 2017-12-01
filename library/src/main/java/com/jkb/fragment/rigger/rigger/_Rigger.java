package com.jkb.fragment.rigger.rigger;

import static com.jkb.fragment.rigger.utils.RiggerConsts.METHOD_GET_CONTAINERVIEWID;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import com.jkb.fragment.rigger.annotation.Puppet;
import com.jkb.fragment.rigger.exception.NotExistException;
import com.jkb.fragment.rigger.exception.RiggerException;
import com.jkb.fragment.rigger.helper.FragmentStackManager;
import java.lang.reflect.Method;
import java.util.Stack;

/**
 * Rigger.Used to repeat different Rigger(Strategy pattern)
 *
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 20,2017
 */

abstract class _Rigger implements IRigger {

  static final String BUNDLE_KEY_FOR_RESULT = "/bundle/key/for/result";

  static _Rigger create(@NonNull Object object) {
    if (object instanceof AppCompatActivity) {
      return new _ActivityRigger((AppCompatActivity) object);
    } else if (object instanceof Fragment) {
      return new _FragmentRigger((Fragment) object);
    } else {
      throw new RiggerException(
          "Puppet Annotation class can only used on android.app.Activity or android.support.v4.app.Fragment");
    }
  }

  //data
  @IdRes
  int mContainerViewId;
  boolean mBindContainerView;
  RiggerTransaction mRiggerTransaction;
  FragmentStackManager mStackManager;

  _Rigger(Object puppetTarget) {
    //init containerViewId
    Class<?> clazz = puppetTarget.getClass();
    Puppet puppet = clazz.getAnnotation(Puppet.class);
    mBindContainerView = puppet.bondContainerView();
    mContainerViewId = puppet.containerViewId();
    if (mContainerViewId <= 0) {
      try {
        Method containerViewId = clazz.getMethod(METHOD_GET_CONTAINERVIEWID);
        mContainerViewId = (int) containerViewId.invoke(puppetTarget);
      } catch (Exception ignored) {
      }
    }
    //init helper
    mStackManager = new FragmentStackManager();
  }

  /**
   * Called when a fragment is first attached to its context.
   * {@link #onCreate(Bundle)} will be called after this.
   */
  public void onAttach(Context context) {
  }

  /**
   * Called when the activity is starting.This is where most initialization should go.
   *
   * @param savedInstanceState If the activity/fragment is being re-created from
   *                           a previous saved state, this is the state.
   */
  public void onCreate(Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      mStackManager = FragmentStackManager.restoreStack(savedInstanceState);
    }
  }

  /**
   * This is the fragment-orientated version of {@link #onResume()} that you
   * can override to perform operations in the Activity at the same point
   * where its fragments are resumed.  Be sure to always call through to
   * the super-class.
   */
  public void onResumeFragments() {
  }

  /**
   * Called after {@link Activity#onRestoreInstanceState}, {@link Activity#onRestart}, or
   * {@link #onPause}, for your activity to start interacting with the user.
   * This is a good place to begin animations
   */
  public abstract void onResume();

  /**
   * Called as part of the activity lifecycle when an activity is going into
   * the background, but has not (yet) been killed.
   */
  public void onPause() {
  }

  /**
   * Called to retrieve per-instance state from an activity before being killed
   * so that the state can be restored in {@link #onCreate}
   *
   * @param outState Bundle in which to place your saved state.
   */
  public abstract void onSaveInstanceState(Bundle outState);

  /**
   * Perform any final cleanup before an activity is destroyed.
   */
  public abstract void onDestroy();

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
  public int getContainerViewId() {
    return mContainerViewId;
  }

  @Override
  public boolean isBondContainerView() {
    return mBindContainerView;
  }

  @Override
  final public Stack<String> getFragmentStack() {
    if (mStackManager == null || mStackManager.getFragmentStack() == null) return new Stack<>();
    return (Stack<String>) mStackManager.getFragmentStack().clone();
  }

  @Override
  public void printStack() {
    StringBuilder sb = new StringBuilder();
    sb.append(getFragmentTAG());
    Stack<String> stack = mStackManager.getFragmentStack();
    printStack(sb, this, stack, 1);
    Log.i("Rigger", sb.toString());
  }

  private void printStack(StringBuilder sb, _Rigger rigger, Stack<String> stack, int level) {
    if (stack == null || stack.empty()) return;
    for (int p = stack.size() - 1; p >= 0; p--) {
      String tag = stack.get(p);
      sb.append("\n");
      sb.append("┃");
      if (level != 1) {
        for (int i = 0; i < level; i++) {
          sb.append(" ").append(" ").append(" ").append(" ");
        }
      }
      for (int i = 0; i < level; i++) {
        sb.append("\t");
      }
      Fragment fragment = rigger.mRiggerTransaction.find(tag);
      _Rigger childRigger = (_Rigger) Rigger.getRigger(fragment);
      Stack<String> childStack = childRigger.getFragmentStack();
      if (p > 0 && childStack.isEmpty()) {
        sb.append("┠");
      } else {
        sb.append("┖");
      }
      sb.append("————");
      sb.append(tag);
      printStack(sb, childRigger, childStack, level + 1);
    }
  }

  /**
   * Throw the exception.
   */
  void throwException(RiggerException e) {
    throw e;
  }
}
