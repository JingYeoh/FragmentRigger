package com.jkb.fragment.rigger.rigger;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import com.jkb.fragment.rigger.utils.Logger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * API for performing a set of Fragment operations.
 *
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 29,2017
 */

abstract class RiggerTransaction {

  /**
   * Finds a fragment that was identified by the given tag either when inflated
   * from XML or as supplied when added in a transaction.
   *
   * @param tag The tag name for the fragment.
   *
   * @return The fragment if found or null otherwise.
   */
  abstract Fragment find(String tag);

  /**
   * Add a fragment to the activity state.
   *
   * @param containerViewId Optional identifier of the container this fragment is to be placed in.
   * @param fragment        The fragment to be added.
   * @param tag             the tag name for the fragment
   *
   * @return Returns the same RiggerTransaction instance.
   */
  abstract RiggerTransaction add(@IdRes int containerViewId, Fragment fragment, @NonNull String tag);

  /**
   * Remove existing fragment.
   *
   * @param tags The tag name for fragment to be removed.
   *
   * @return Returns the same RiggerTransaction instance.
   */
  abstract RiggerTransaction remove(String... tags);

  /**
   * Remove all existing fragment.
   *
   * @return Returns the same RiggerTransaction instance.
   */
  abstract RiggerTransaction removeAll();

  /**
   * Show previously hidden fragments.
   *
   * @param tags The tag name for fragment to be shown.
   *
   * @return Returns the same RiggerTransaction instance.
   */
  abstract RiggerTransaction show(String... tags);

  /**
   * Hide existing fragment.
   *
   * @param tags The tag name for fragment to be hidden.
   *
   * @return Returns the same RiggerTransaction instance.
   */
  abstract RiggerTransaction hide(String... tags);

  /**
   * Schedules a commit of this transaction.  The commit does
   * not happen immediately; it will be scheduled as work on the main thread
   * to be done the next time that thread is ready.
   */
  abstract void commit();

  /**
   * Returns the value of transaction count is 0.
   */
  abstract boolean isEmpty();
}

/**
 * Entry of an operation on the fragment.
 */
final class RiggerTransactionImpl extends RiggerTransaction {

  private FragmentManager mFragmentManager;
  private _Rigger mRigger;

  static final int OP_NULL = 0;
  static final int OP_ADD = 1;
  static final int OP_REPLACE = 2;
  static final int OP_REMOVE = 3;
  static final int OP_HIDE = 4;
  static final int OP_SHOW = 5;
  static final int OP_DETACH = 6;
  static final int OP_ATTACH = 7;

  private static final class Op {

    Op next;
    Op prev;
    int cmd;
    Fragment fragment;
    String fragmentTag;
    int containerViewId;
  }

  private Op mHead;
  private Op mTail;
  private int mNumOp;
  private LinkedList<Op> mTransactions;
  private Map<String, Fragment> mAdded;

  RiggerTransactionImpl(_Rigger rigger, FragmentManager mFragmentManager) {
    this.mFragmentManager = mFragmentManager;
    this.mRigger = rigger;
    mAdded = new HashMap<>();
  }

  private void addOp(Op op) {
    if (mHead == null) {
      mHead = mTail = op;
    } else {
      op.prev = mTail;
      mTail.next = op;
      mTail = op;
    }
    mNumOp++;
  }

  @Override
  Fragment find(String tag) {
    if (TextUtils.isEmpty(tag)) return null;
    Fragment fragment = mAdded.get(tag);
    if (fragment == null) {
      fragment = mFragmentManager.findFragmentByTag(tag);
    }
    return fragment;
  }

  @Override
  RiggerTransaction add(@IdRes int containerViewId, Fragment fragment, @NonNull String tag) {
    Op op = new Op();
    op.cmd = OP_ADD;
    op.fragment = fragment;
    op.fragmentTag = tag;
    op.containerViewId = containerViewId;
    addOp(op);
    return this;
  }

  @Override
  RiggerTransaction remove(String... tags) {
    if (tags == null || tags.length == 0) return this;
    for (String tag : tags) {
      Op op = new Op();
      op.cmd = OP_REMOVE;
      op.fragmentTag = tag;
      addOp(op);
    }
    return this;
  }

  @Override
  RiggerTransaction removeAll() {
    if (mAdded == null || mAdded.isEmpty()) return this;
    for (String s : mAdded.keySet()) {
      remove(s);
    }
    return this;
  }

  @Override
  RiggerTransaction show(String... tags) {
    if (tags == null || tags.length == 0) return this;
    for (String tag : tags) {
      Op op = new Op();
      op.cmd = OP_SHOW;
      op.fragmentTag = tag;
      addOp(op);
    }
    return this;
  }

  @Override
  RiggerTransaction hide(String... tags) {
    if (tags == null || tags.length == 0) return this;
    for (String tag : tags) {
      Op op = new Op();
      op.cmd = OP_HIDE;
      op.fragmentTag = tag;
      addOp(op);
    }
    return this;
  }

  @Override
  void commit() {
    //add the transaction to list.
    if (mTransactions == null) {
      mTransactions = new LinkedList<>();
    }
    if (mHead != null) {
      Op op = mHead;
      mTransactions.add(op);
    }

    //clear the link list.
    mHead = mTail = null;
    mNumOp = 0;

    //initiating the real commit or wait to the next time.
    executePendingTransaction();
  }

  @Override
  boolean isEmpty() {
    return mNumOp == 0;
  }

  /**
   * executing the top transaction operation of link list if it's ready to commit;
   */
  private void executePendingTransaction() {
    if (!mRigger.isResumed()) {
      Logger.w(this, "the rigger is not resumed,the commit will be delayed");
      return;
    }
    Op op = mTransactions.poll();
    if (op == null) return;
    FragmentTransaction ft = mFragmentManager.beginTransaction();
    while (op != null) {
      switch (op.cmd) {
        case OP_ADD: {
          ft.add(op.containerViewId, op.fragment, op.fragmentTag);
          mAdded.put(op.fragmentTag, op.fragment);
        }
        break;
        case OP_REMOVE: {
          Fragment f = find(op.fragmentTag);
          ft.remove(f);
          mAdded.remove(op.fragmentTag);
        }
        break;
        case OP_SHOW: {
          Fragment f = find(op.fragmentTag);
          ft.show(f);
        }
        break;
        case OP_HIDE: {
          Fragment f = find(op.fragmentTag);
          ft.hide(f);
        }
        break;
      }
      op = op.next;
    }
    ft.commit();
    executePendingTransaction();
  }
}