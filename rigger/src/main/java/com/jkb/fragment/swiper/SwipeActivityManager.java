package com.jkb.fragment.swiper;

import android.app.Activity;

import androidx.annotation.NonNull;

import java.util.Stack;

/**
 * This class is used to manage {@link android.app.Activity} and only used by {@link SwipeLayout}.
 *
 * @author JingYeoh
 * <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 * <a href="https://github.com/justkiddingbaby">Github</a>
 * <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Aug 1,2018
 */
class SwipeActivityManager {

    private Stack<Activity> mActivityStack;

    private static volatile SwipeActivityManager sInstance = null;

    private SwipeActivityManager() {
        mActivityStack = new Stack<>();
    }

    /**
     * Returns the instance of SwipeManager.
     */
    static SwipeActivityManager getInstance() {
        if (sInstance == null) {
            synchronized (SwipeActivityManager.class) {
                if (sInstance == null) {
                    sInstance = new SwipeActivityManager();
                }
            }
        }
        return sInstance;
    }

    private void addToStack(@NonNull Activity activity) {
        if (mActivityStack.contains(activity)) {
            return;
        }
        mActivityStack.add(activity);
    }

    private void removeFromStack(@NonNull Activity activity) {
        mActivityStack.remove(activity);
    }

    @NonNull
    Stack<Activity> getActivityStack() {
        return mActivityStack;
    }
}
