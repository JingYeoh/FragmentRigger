package com.jkb.fragment.rigger.model;

import android.os.Bundle;
import java.io.Serializable;
import java.util.Stack;

/**
 * Fragment Stack Manager.Used to save/get/remove/restore and more operations for Fragment Stack.
 *
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 20,2017
 */

public final class FragmentStackManager implements Cloneable, Serializable {

  private static final String BUNDLE_KEY_FRAGMENT_STACK = "/bundle/key/fragment/stack";

  /**
   * Fragment stack.save the fragment tags that is added in FragmentManager for Activity/Fragment.
   */
  private Stack<String> mFragmentStack;

  public FragmentStackManager() {
    mFragmentStack = new Stack<>();
  }

  /**
   * Returns the fragment stack.
   */
  public Stack<String> getFragmentStack() {
    return mFragmentStack;
  }

  /**
   * Restore and return the fragment stack.
   */
  public static FragmentStackManager restoreStack(Bundle savedInstanceState) {
    return (FragmentStackManager) savedInstanceState.getSerializable(BUNDLE_KEY_FRAGMENT_STACK);
  }

  /**
   * Called to retrieve per-instance state from an activity/fragment before being killed
   * so that the state can be restored in {@link #restoreStack(Bundle)}
   */
  public void saveInstanceState(Bundle savedInstanceState) {
    savedInstanceState.putSerializable(BUNDLE_KEY_FRAGMENT_STACK, this);
  }
}
