package com.jkb.fragment.rigger.rigger;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import com.jkb.fragment.rigger.utils.Logger;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Entry of an operation on the fragment.
 */
final class RiggerTransactionImpl extends RiggerTransaction {

    private FragmentManager mFragmentManager;
    private IRigger mRigger;

    private static final int OP_NULL = 0;
    private static final int OP_ADD = 1;
    private static final int OP_REPLACE = 2;
    private static final int OP_REMOVE = 3;
    private static final int OP_HIDE = 4;
    private static final int OP_SHOW = 5;
    private static final int OP_DETACH = 6;
    private static final int OP_ATTACH = 7;

    private static final class Op {

        Op next;
        Op prev;
        int cmd;
        Fragment fragment;
        String fragmentTag;
        int containerViewId;
        int enterAnim;
        int exitAnim;
    }

    private Op mHead;
    private Op mTail;
    private int mNumOp;
    private int mEnterAnim;
    private int mExitAnim;
    private LinkedList<Op> mTransactions;
    private Map<String, WeakReference<Fragment>> mAdded;

    RiggerTransactionImpl(IRigger rigger, FragmentManager mFragmentManager) {
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
        op.enterAnim = mEnterAnim;
        op.exitAnim = mExitAnim;
        mNumOp++;
    }

    @Override
    Fragment find(String tag) {
        if (TextUtils.isEmpty(tag)) return null;
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment == null && mAdded.containsKey(tag)) {
            fragment = mAdded.get(tag).get();
            if (fragment == null) {
                mAdded.remove(tag);
            }
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
        //add to list.
        if (!mAdded.containsKey(tag)) {
            mAdded.put(tag, new WeakReference<>(fragment));
        }
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
    void setCustomAnimations(int enter, int exit) {
        mEnterAnim = enter;
        mExitAnim = exit;
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
            Logger.w(this, "the puppet is not resumed, the commit will be delayed");
            return;
        }
        Op op = mTransactions.poll();
        if (op == null) return;
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        while (op != null) {
            switch (op.cmd) {
                case OP_ADD: {
                    ft.setCustomAnimations(op.enterAnim, op.exitAnim);
                    ft.add(op.containerViewId, op.fragment, op.fragmentTag);
                }
                break;
                case OP_REMOVE: {
                    Fragment f = find(op.fragmentTag);
                    if (f == null) {
                        Logger.w(this, "Op:Remove. can not find fragment " + op.fragmentTag);
                    } else {
                        ft.remove(f);
                        mAdded.remove(op.fragmentTag);
                    }
                }
                break;
                case OP_SHOW: {
                    ft.setCustomAnimations(op.enterAnim, op.exitAnim);
                    Fragment f = find(op.fragmentTag);
                    if (f == null) {
                        Logger.w(this, "Op:Show. can not find fragment " + op.fragmentTag);
                    } else {
                        ft.show(f);
                    }
                }
                break;
                case OP_HIDE: {
                    ft.setCustomAnimations(op.enterAnim, op.exitAnim);
                    Fragment f = find(op.fragmentTag);
                    if (f == null) {
                        Logger.w(this, "Op:Hide. can not find fragment " + op.fragmentTag);
                    } else {
                        ft.hide(f);
                    }
                }
                break;
                case OP_ATTACH: {
                    Fragment f = find(op.fragmentTag);
                    if (f == null) {
                        Logger.w(this, "Op:Attach. can not find fragment " + op.fragmentTag);
                    } else {
                        ft.attach(f);
                    }
                }
                break;
                case OP_DETACH: {
                    Fragment f = find(op.fragmentTag);
                    if (f == null) {
                        Logger.w(this, "Op:Detach. can not find fragment " + op.fragmentTag);
                    } else {
                        ft.detach(f);
                    }
                }
                break;
            }
            op = op.next;
        }
        ft.commit();
        executePendingTransaction();
    }
}
