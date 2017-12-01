package com.jkb.fragment.rigger.rigger;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import com.jkb.fragment.rigger.annotation.Puppet;
import com.jkb.fragment.rigger.exception.AlreadyExistException;
import java.util.Stack;

/**
 * Rigger interface class.used to define the methods can be called.
 *
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 20,2017
 */

public interface IRigger {

  /**
   * Take care of popping the fragment back stack or finishing the activity
   * as appropriate.the method is called when you click the back key.
   */
  void onRiggerBackPressed();

  /**
   * Adds and show a fragment in parent's containerView and hide other fragments that is placed in the
   * containerView.
   *
   * @param fragment the fragment that will be showed.
   */
  void startFragment(@NonNull Fragment fragment);

  /**
   * Adds and shows a fragment for which you would like a result when it closed.
   * When this fragment is exists,your onFragmentResult() method will be called with the given requestCode.
   *
   * @param receive     The receive object of onFragmentResult method called back.
   * @param fragment    The fragment to be started.
   * @param requestCode If >= 0,this code will be returned in onFragmentResult() method when fragment exits.
   */
  void startFragmentForResult(Object receive, @NonNull Fragment fragment, int requestCode);

  /**
   * Adds and shows a fragment for which you would like a result when it closed.
   * When this fragment is exists,your onFragmentResult() method will be called with the given requestCode.
   * The onFragmentResult() method might not be called in the class that called this method, onFragmentResult() method
   * will be called in the class that have the fragment's container.
   *
   * @param fragment    The fragment to be started.
   * @param requestCode If >= 0,this code will be returned in onFragmentResult() method when fragment exits.
   */
  void startFragmentForResult(@NonNull Fragment fragment, int requestCode);

  /**
   * Shows the pop fragment in the stack and hide the others.if the stack is empty,do none operation.
   */
  void startTopFragment();

  /**
   * Shows a fragment and hide the others which is contained in the containerView.if the fragment is not added in the
   * stack,then add first.
   *
   * The fragment added by this method is not pushed in the stack.if the method {@link #onRiggerBackPressed()} is
   * called, this fragment has none operation.
   *
   * @param fragment        the fragment to be showed.
   * @param containerViewId the fragment's container view's id.
   */
  void showFragment(@NonNull Fragment fragment, @IdRes int containerViewId);

  /**
   * Shows an existing fragment and hide the others which is contained in the containerView.
   *
   * @param tag the tag name of fragment to be showed.
   */
  void showFragment(@NonNull String tag);

  /**
   * Hides an existing fragment.
   *
   * @param fragment The fragment to be hidden.
   */
  void hideFragment(@NonNull Fragment fragment);

  /**
   * Hides an existing fragment and hide the others which is contained in the containerView.
   *
   * @param tag the tag name of fragment to be hidden.
   */
  void hideFragment(@NonNull String tag);

  /**
   * Shows a fragment and remove the others which is contained in the containerView.
   * if the fragment is not added in the stack,then add first,
   * if the fragment is already exist,then throw {@link AlreadyExistException}.
   *
   * The fragment added by this method is not pushed in the stack.if the method {@link #onRiggerBackPressed()} is
   * called, this fragment has none operation.
   *
   * @param fragment        the fragment to be showed.
   * @param containerViewId the fragment's container view's id.
   */
  void replaceFragment(@NonNull Fragment fragment, @IdRes int containerViewId);

  /**
   * Returns the resume status of Activity/Fragment.
   *
   * @return is or not resumed.
   */
  boolean isResumed();

  /**
   * Closes the current Activity/Fragment.if this method is called by {@link Fragment},then this fragment will be
   * removed from the parent's fragment stack.
   */
  void close();

  /**
   * Closes a fragment and remove it from stack.
   *
   * @param fragment the fragment that will be finished.this is the current's child fragment.
   */
  void close(@NonNull Fragment fragment);

  /**
   * Returns the tag of fragment.if the method is called by Activity,then return null.
   */
  String getFragmentTAG();

  /**
   * Returns the optional identifier of the container this fragment is placed in.
   */
  @IdRes
  int getContainerViewId();

  /**
   * Returns the value of bondContainerView in {@link Puppet}.
   */
  boolean isBondContainerView();

  /**
   * Call this to set the result that your fragment will return to its
   * caller.
   *
   * @param resultCode The result code to propagate back to the originating
   *                   fragment, often RESULT_CANCELED or RESULT_OK
   * @param bundle     The data to propagate back to the originating fragment.
   *
   * @see Rigger#RESULT_OK
   * @see Rigger#RESULT_CANCELED
   */
  void setResult(int resultCode, Bundle bundle);

  /**
   * Returns the current fragment stack.
   */
  Stack<String> getFragmentStack();

  /**
   * Printf the current stack in logcat.
   */
  void printStack();
}
