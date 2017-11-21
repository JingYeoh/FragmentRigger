package com.jkb.fragment.rigger.rigger;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import com.jkb.fragment.rigger.exception.RiggerException;

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

  /**
   * Called when a fragment is first attached to its context.
   * {@link #onCreate(Bundle)} will be called after this.
   */
  public abstract void onAttach(Context context);

  /**
   * Called when the activity is starting.This is where most initialization should go.
   *
   * @param savedInstanceState If the activity/fragment is being re-created from
   *                           a previous saved state, this is the state.
   */
  public abstract void onCreate(Bundle savedInstanceState);

  /**
   * This is the fragment-orientated version of {@link #onResume()} that you
   * can override to perform operations in the Activity at the same point
   * where its fragments are resumed.  Be sure to always call through to
   * the super-class.
   */
  public abstract void onResumeFragments();

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
  public abstract void onPause();

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
}
