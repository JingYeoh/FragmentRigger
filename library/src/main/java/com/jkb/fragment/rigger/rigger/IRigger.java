package com.jkb.fragment.rigger.rigger;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

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
   * start show a fragment id parent's containerViewId and hide all fragments that is contained in the containerViewId.
   *
   * @param fragment the fragment that will be showed.
   */
  void startFragment(@NonNull Fragment fragment);

  /**
   * return the resume status of Activity/Fragment.
   *
   * @return is or not resumed.
   */
  boolean isResumed();

  /**
   * close current Activity/Fragment.if this method is called by {@link Fragment},then this fragment will be removed
   * from the parent's fragment stack.
   */
  void close();

  /**
   * close the fragment and remove this fragment from stack.
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
}
