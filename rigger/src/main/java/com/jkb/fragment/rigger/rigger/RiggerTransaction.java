package com.jkb.fragment.rigger.rigger;


import androidx.fragment.app.Fragment;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

/**
 * API for performing a set of Fragment operations.
 *
 * @author JingYeoh
 * <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 * <a href="https://github.com/justkiddingbaby">Github</a>
 * <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 29,2017
 */

abstract class RiggerTransaction {

    /**
     * Finds a fragment that was identified by the given tag either when inflated
     * from XML or as supplied when added in a transaction.
     *
     * @param tag The tag name for the fragment.
     * @return The fragment if found or null otherwise.
     */
    abstract Fragment find(String tag);

    /**
     * Add a fragment to the activity state.
     *
     * @param containerViewId Optional identifier of the container this fragment is to be placed in.
     * @param fragment        The fragment to be added.
     * @param tag             the tag name for the fragment
     * @return Returns the same RiggerTransaction instance.
     */
    abstract RiggerTransaction add(@IdRes int containerViewId, Fragment fragment, @NonNull String tag);

    /**
     * Remove existing fragment.
     *
     * @param tags The tag name for fragment to be removed.
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
     * @return Returns the same RiggerTransaction instance.
     */
    abstract RiggerTransaction show(String... tags);

    /**
     * Hide existing fragment.
     *
     * @param tags The tag name for fragment to be hidden.
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
     * Set the animations for fragment,the animations will be showed when
     * {@link androidx.fragment.app.FragmentTransaction#show(Fragment)} or
     * {@link androidx.fragment.app.FragmentTransaction#hide(Fragment)} is called.
     *
     * @param enter the animation when the fragment is showing.
     * @param exit  the animation when the fragment is hiding.
     */
    abstract void setCustomAnimations(int enter, int exit);

    /**
     * Returns the value of transaction count is 0.
     */
    abstract boolean isEmpty();
}

