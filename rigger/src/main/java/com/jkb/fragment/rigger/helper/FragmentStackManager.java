package com.jkb.fragment.rigger.helper;

import  androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Stack;

/**
 * Fragment Stack Manager.Used to save/get/remove/restore and more operations for Fragment Stack.
 *
 * @author JingYeoh
 * <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 * <a href="https://github.com/justkiddingbaby">Github</a>
 * <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 20,2017
 */

public final class FragmentStackManager implements Cloneable, Serializable {

    private static final String BUNDLE_KEY_FRAGMENT_STACK = "/bundle/key/fragment/stack";

    /**
     * Fragment manager of the host.
     */
    private transient FragmentManager mFm;

    /**
     * Fragment stack.save the fragment tags that is added in FragmentManager for Activity/Fragment.
     */
    private Stack<String> mFragmentStack;

    /**
     * Used to save Fragment's tag and containerViewId.
     */
    private HashMap<String, Integer> mFragmentContainerMap;

    public FragmentStackManager() {
        mFragmentStack = new Stack<>();
        mFragmentContainerMap = new HashMap<>();
    }

    /**
     * bind FragmentManager object for host.
     */
    public void bindFragmentManager(FragmentManager fm) {
        mFm = fm;
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

    /**
     * Pushes the specified object onto the top of the stack.
     *
     * @param fragmentTag     Fragment's tag
     * @param containerViewId the containerView that fragment is added.
     */
    public boolean push(String fragmentTag, @IdRes int containerViewId) {
        if (mFragmentStack.contains(fragmentTag)) return false;
        mFragmentStack.push(fragmentTag);
        mFragmentContainerMap.put(fragmentTag, containerViewId);
        return true;
    }

    /**
     * Adds a fragment onto the list.
     */
    public boolean add(String fragmentTag, @IdRes int containerViewId) {
        if (contain(fragmentTag)) return false;
        mFragmentContainerMap.put(fragmentTag, containerViewId);
        return true;
    }

    /**
     * Returns the fragmentTag at the top of the stack and removes it.
     */
    public String pop() {
        String fragmentTag = null;
        if (!mFragmentStack.empty()) {
            fragmentTag = mFragmentStack.pop();
            mFragmentContainerMap.remove(fragmentTag);
        }
        return fragmentTag;
    }

    /**
     * Returns the fragmentTag at the top of the stack.
     */
    public String peek() {
        String fragmentTag = null;
        if (!mFragmentStack.empty()) {
            fragmentTag = mFragmentStack.peek();
        }
        return fragmentTag;
    }

    /**
     * Returns the value of if the fragment is in the stack.
     */
    public boolean contain(String fragmentTag) {
        return (mFragmentContainerMap != null) && mFragmentContainerMap.containsKey(fragmentTag);
    }

    /**
     * Returns the container view id of the fragment.
     *
     * @param tag the tag name for fragment to be found.
     *
     * @return 0 the default content.the effective id otherwise.
     */
    public int getContainer(String tag) {
        if (!contain(tag)) return 0;
        return mFragmentContainerMap.get(tag);
    }

    /**
     * Removes the specified object onto the top of the stack.
     *
     * @param fragmentTag the fragment that will be removed.
     */
    public boolean remove(String fragmentTag) {
        if (TextUtils.isEmpty(fragmentTag)) return false;
        if (!contain(fragmentTag)) return false;
        mFragmentStack.remove(fragmentTag);
        mFragmentContainerMap.remove(fragmentTag);
        return true;
    }

    /**
     * Remove the specified objects onto the stack which is placed into the container view.
     *
     * @param containerViewId the container view that fragment is placed in.
     */
    public boolean remove(@IdRes int containerViewId) {
        Iterator<Entry<String, Integer>> iterator = mFragmentContainerMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, Integer> entry = iterator.next();
            if (entry.getValue() == containerViewId) {
                mFragmentStack.remove(entry.getKey());
                iterator.remove();
            }
        }
        return true;
    }

    /**
     * Return all fragments which is not contained in the stack.
     */
    @NonNull
    public String[] getFragmentsWithoutStack() {
        List<String> fragmentTags = new ArrayList<>();
        for (Entry<String, Integer> entry : mFragmentContainerMap.entrySet()) {
            if (!mFragmentStack.contains(entry.getKey())) {
                fragmentTags.add(entry.getKey());
            }
        }
        // The fragment not added by Rigger should be ignored,otherwise the back pressed can not be processed.
//        if (mFm != null) {
//            List<Fragment> fragments = mFm.getFragments();
//            if (fragments != null) {
//                for (Fragment fragment : fragments) {
//                    if (fragment == null) continue;
//                    String tag = fragment.getTag();
//                    if(TextUtils.isEmpty(tag))continue;
//                    if (!mFragmentStack.contains(tag) &&
//                            !fragmentTags.contains(tag)) {
//                        fragmentTags.add(tag);
//                    }
//                }
//            }
//        }
        return fragmentTags.toArray(new String[fragmentTags.size()]);
    }

    /**
     * Return all fragments which is contained in the containerView.
     *
     * @param containerViewId the container view's id
     */
    @NonNull
    public String[] getFragmentTags(@IdRes int containerViewId) {
        List<String> fragmentTags = new ArrayList<>();
        for (Entry<String, Integer> item : mFragmentContainerMap.entrySet()) {
            if (item.getValue() == containerViewId) fragmentTags.add(item.getKey());
        }
        return fragmentTags.toArray(new String[fragmentTags.size()]);
    }

    /**
     * Returns true if the tag is contained into the stack.
     *
     * @param tag the fragment tag to be examined
     *
     * @return true if the tag is contained into the stack.
     */
    public boolean isFragmentIntoStack(String tag) {
        return !TextUtils.isEmpty(tag) && mFragmentStack.contains(tag);
    }

    /**
     * Perform any final cleanup before an activity/fragment is destroyed.
     */
    public void onDestroy() {
        mFm = null;
    }

    /**
     * Clear the fragment stack.
     */
    public void clear() {
        mFragmentStack.clear();
        mFragmentContainerMap.clear();
    }
}
