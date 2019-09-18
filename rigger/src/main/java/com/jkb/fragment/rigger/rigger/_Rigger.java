package com.jkb.fragment.rigger.rigger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.jkb.fragment.rigger.annotation.Puppet;
import com.jkb.fragment.rigger.exception.AlreadyExistException;
import com.jkb.fragment.rigger.exception.NotExistException;
import com.jkb.fragment.rigger.exception.RiggerException;
import com.jkb.fragment.rigger.exception.UnSupportException;
import com.jkb.fragment.rigger.helper.FragmentStackManager;
import com.jkb.fragment.rigger.utils.Logger;
import com.jkb.fragment.swiper.SwipeLayout;
import com.jkb.fragment.swiper.annotation.Swiper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.jkb.fragment.rigger.utils.RiggerConsts.METHOD_GET_CONTAINERVIEWID;
import static com.jkb.fragment.rigger.utils.RiggerConsts.METHOD_ON_INTERRUPT_BACKPRESSED;

/**
 * Rigger.Used to repeat different Rigger(Strategy pattern)
 *
 * @author JingYeoh
 * <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 * <a href="https://github.com/justkiddingbaby">Github</a>
 * <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 20,2017
 */

abstract class _Rigger implements IRigger {

    static final String BUNDLE_KEY_FOR_RESULT = "/bundle/key/for/result";
    static final String BUNDLE_KEY_FOR_RESULT_RECEIVE = BUNDLE_KEY_FOR_RESULT + 1;
    static final String BUNDLE_KEY_FOR_RESULT_REQUEST_CODE = BUNDLE_KEY_FOR_RESULT + 2;

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

    private Object mPuppetTarget;
    Context mContext;
    //data
    @IdRes
    private int mContainerViewId;
    private boolean mStickyStack;
    RiggerTransaction mRiggerTransaction;
    FragmentStackManager mStackManager;
    // swiper
    private Swiper mSwiper;
    private SwipeLayout mSwipeLayout;

    _Rigger(Object puppetTarget) {
        this.mPuppetTarget = puppetTarget;
        //init containerViewId
        Class<?> clazz = mPuppetTarget.getClass();
        Puppet puppet = clazz.getAnnotation(Puppet.class);
        mStickyStack = puppet.stickyStack();
        mContainerViewId = puppet.containerViewId();
        if (mContainerViewId <= 0) {
            try {
                Method containerViewId = clazz.getMethod(METHOD_GET_CONTAINERVIEWID);
                mContainerViewId = (int) containerViewId.invoke(mPuppetTarget);
            } catch (Exception ignored) {
            }
        }
        // init swiper
        mSwiper = clazz.getAnnotation(Swiper.class);
        //init helper
        mStackManager = new FragmentStackManager();
    }

    @NonNull
    @Override
    public Object getPuppetHost() {
        if (mPuppetTarget instanceof Fragment) {
            Fragment fragment = (Fragment) mPuppetTarget;
            Fragment parent = fragment.getParentFragment();
            while (true) {
                if (parent == null) break;
                IRigger rigger = Rigger.getRigger(parent);
                String[] stack = ((_Rigger) rigger).mStackManager.getFragmentsWithoutStack();
                for (String tag : stack) {
                    if (tag.equals(getFragmentTAG())) {
                        return parent;
                    }
                }
                int containerViewId = rigger.getContainerViewId();
                if (containerViewId > 0) break;
                parent = parent.getParentFragment();
            }
            if (parent == null) {
                Object host = fragment.getHost();
                return host == null ? mPuppetTarget : host;
            }
            return parent;
        } else {
            return mPuppetTarget;
        }
    }

    /**
     * Called when a fragment is first attached to its context.
     * {@link #onCreate(Bundle)} will be called after this.
     */
    void onAttach(Context context) {
    }

    /**
     * Called when the activity is starting.This is where most initialization should go.
     *
     * @param savedInstanceState If the activity/fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mStackManager = FragmentStackManager.restoreStack(savedInstanceState);
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState, @Nullable View view) {
        return view;
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle, View)}
     * has returned, but before any saved state has been restored in to the view.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle, View)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }

    /**
     * This is the fragment-orientated version of {@link #onResume()} that you
     * can override to perform operations in the Activity at the same point
     * where its fragments are resumed.  Be sure to always call through to
     * the super-class.
     */
    void onResumeFragments() {
    }

    /**
     * Called after {@link Activity#onRestoreInstanceState}, {@link Activity#onRestart}, or
     * {@link #onPause}, for your activity to start interacting with the user.
     * This is a good place to begin mAnimations
     */
    abstract void onResume();

    /**
     * Called as part of the activity lifecycle when an activity is going into
     * the background, but has not (yet) been killed.
     */
    void onPause() {
    }

    /**
     * Called to retrieve per-instance state from an activity before being killed
     * so that the state can be restored in {@link #onCreate}
     *
     * @param outState Bundle in which to place your saved state.
     */
    abstract void onSaveInstanceState(Bundle outState);

    /**
     * Perform any final cleanup before an activity is destroyed.
     */
    abstract void onDestroy();

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    void onDetach() {
    }

    /**
     * Set a hint to the system about whether this fragment's UI is currently visible
     * to the user. This hint defaults to true and is persistent across fragment instance
     * state save and restore.
     *
     * @param isVisibleToUser true if this fragment's UI is currently visible to the user (default),
     *                        false if it is not.
     */
    void setUserVisibleHint(boolean isVisibleToUser) {
    }

    /**
     * Pass the touch back key event down.
     */
    void dispatchBackPressed() {
        Logger.d(mPuppetTarget, "dispatchBackPressed() method is called");
        boolean isInterrupt = onInterruptBackPressed();
        if (isInterrupt) {
            onBackPressed();
        } else {
            //call the fragment's dispatchBackPressed method that is not contained into the stack.
            String[] fragmentsWithoutStack = mStackManager.getFragmentsWithoutStack();
            for (String tag : fragmentsWithoutStack) {
                Fragment fragmentWithoutStack = mRiggerTransaction.find(tag);
                if (fragmentWithoutStack == null) {
                    throwException(new NotExistException(tag));
                }
                ((_Rigger) Rigger.getRigger(fragmentWithoutStack)).dispatchBackPressed();
            }

            String topFragmentTag = mStackManager.peek();
            if (!TextUtils.isEmpty(topFragmentTag)) {
                Fragment topFragment = mRiggerTransaction.find(topFragmentTag);
                if (topFragment == null) {
                    throwException(new NotExistException(topFragmentTag));
                }
                //call the top fragment's onBackPressed method.
                ((_Rigger) Rigger.getRigger(topFragment)).dispatchBackPressed();
            }

            // if this host contain non fragments , call onRiggerBackPressed method.
            if (fragmentsWithoutStack.length == 0 && TextUtils.isEmpty(topFragmentTag)) {
                onBackPressed();
            }
        }
    }

    /**
     * If the puppet contain onInterruptBackPressed method , then interrupt the {@link #onBackPressed()} method.
     */
    private boolean onInterruptBackPressed() {
        Logger.d(mPuppetTarget, "onInterruptBackPressed() method is called");
        Class<?> clazz = mPuppetTarget.getClass();
        try {
            Method onBackPressed = clazz.getMethod(METHOD_ON_INTERRUPT_BACKPRESSED);
            return (boolean) onBackPressed.invoke(mPuppetTarget);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        Logger.d(mPuppetTarget, "onRiggerBackPressed() method is called");
        String topFragmentTag = mStackManager.peek();
        if (TextUtils.isEmpty(topFragmentTag)) {
            close();
        } else {
            //call the top fragment's onBackPressed method.
            Fragment topFragment = mRiggerTransaction.find(topFragmentTag);
            if (topFragment == null) {
                throwException(new NotExistException(topFragmentTag));
            }
            ((_Rigger) Rigger.getRigger(topFragment)).dispatchBackPressed();
        }
    }

    @Override
    public Fragment findFragmentByTag(String tag) {
        if (!mStackManager.contain(tag)) return null;
        return mRiggerTransaction.find(tag);
    }

    @Override
    public void addFragment(@IdRes int containerViewId, Fragment... fragments) {
        if (fragments == null) {
            Logger.w(mPuppetTarget, "the fragments to be added is null.");
            return;
        }
        for (Fragment fragment : fragments) {
            String fragmentTAG = Rigger.getRigger(fragment).getFragmentTAG();
            if (mStackManager.add(fragmentTAG, containerViewId)) {
                addFragmentWithAnim(fragment, containerViewId);
                mRiggerTransaction.hide(fragmentTAG);
            } else {
                throwException(new AlreadyExistException(fragmentTAG));
            }
            fragment.setUserVisibleHint(false);
        }
        mRiggerTransaction.commit();
    }

    @SuppressLint("ResourceType")
    @Override
    public void startFragment(@NonNull Fragment fragment) {
        String fragmentTAG = Rigger.getRigger(fragment).getFragmentTAG();
        if (!mStackManager.push(fragmentTAG, mContainerViewId)) {
            throwException(new AlreadyExistException(fragmentTAG));
        }
        if (getContainerViewId() <= 0) {
            throwException(
                    new UnSupportException("ContainerViewId must be effective in class " + mPuppetTarget.getClass()));
        }
        addFragmentWithAnim(fragment, mContainerViewId);
        mRiggerTransaction.hide(getVisibleFragmentTags(getContainerViewId()));
        mRiggerTransaction.show(fragmentTAG).commit();
    }

    @Override
    public void startFragmentForResult(Object receive, @NonNull Fragment fragment, int requestCode) {
        Bundle arguments = fragment.getArguments();
        if (arguments == null) arguments = new Bundle();
        Bundle receiveArgs = new Bundle();
        if (receive != null) {
            receiveArgs.putString(BUNDLE_KEY_FOR_RESULT_RECEIVE, Rigger.getRigger(receive).getFragmentTAG());
        }
        receiveArgs.putInt(BUNDLE_KEY_FOR_RESULT_REQUEST_CODE, requestCode);
        arguments.putParcelable(BUNDLE_KEY_FOR_RESULT, receiveArgs);
        fragment.setArguments(arguments);
        startFragment(fragment);
    }

    @Override
    public void startFragmentForResult(@NonNull Fragment fragment, int requestCode) {
        startFragmentForResult(null, fragment, requestCode);
    }

    @Override
    public void startPopFragment() {
        startPopFragment(null);
    }

    /**
     * show pop fragment and start animation.
     */
    void startPopFragment(Animation animation) {
        String topFragmentTag = mStackManager.peek();
        mRiggerTransaction.hide(getVisibleFragmentTags(getContainerViewId()));
        Fragment topFragment = mRiggerTransaction.find(topFragmentTag);
        if (!TextUtils.isEmpty(topFragmentTag) && topFragment != null) {
            if (animation != null) {
                View view = topFragment.getView();
                if (view != null) {
                    view.startAnimation(animation);
                }
                //cancel the default animation and use the custom animation.
            }
            mRiggerTransaction.setCustomAnimations(0, 0);
            mRiggerTransaction.show(topFragmentTag);
        }
        mRiggerTransaction.commit();
    }

    @Override
    public void showFragment(@NonNull Fragment fragment, @IdRes int containerViewId) {
        showFragment(fragment, containerViewId, false);
    }

    @Override
    public void showFragment(@NonNull Fragment fragment, @IdRes int containerViewId, boolean showRepeatAnim) {
        String fragmentTAG = Rigger.getRigger(fragment).getFragmentTAG();
        if (mStackManager.add(fragmentTAG, containerViewId)) {
            addFragmentWithAnim(fragment, containerViewId);
        }
        String[] fragmentTags = mStackManager.getFragmentTags(containerViewId);
        for (String tag : fragmentTags) {
            Fragment hideFrag = mRiggerTransaction.find(tag);
            if (hideFrag == null) continue;
            hideFrag.setUserVisibleHint(false);
        }
        fragment.setUserVisibleHint(true);
        boolean hidden = fragment.isHidden();
        boolean added = fragment.isAdded();
        if (!added || hidden || showRepeatAnim) {
            mRiggerTransaction.hide(getVisibleFragmentTags(containerViewId));
            showFragmentWithAnim(fragment);
        }
        mRiggerTransaction.commit();
    }

    @Override
    public void showFragment(@NonNull String tag) {
        showFragment(tag, false);
    }

    @Override
    public void showFragment(@NonNull String tag, boolean showRepeatAnim) {
        int containerViewId = mStackManager.getContainer(tag);
        if (containerViewId == 0) {
            throwException(new NotExistException(tag));
        }
        showFragment(mRiggerTransaction.find(tag), containerViewId, showRepeatAnim);
    }

    @Override
    public void hideFragment(@NonNull Fragment fragment) {
        _FragmentRigger rigger = (_FragmentRigger) Rigger.getRigger(fragment);
        String fragmentTAG = rigger.getFragmentTAG();
        mRiggerTransaction.setCustomAnimations(rigger.mPopEnterAnim, rigger.mExitAnim);
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
        addFragmentWithAnim(fragment, containerViewId);
        mRiggerTransaction.remove(mStackManager.getFragmentTags(containerViewId))
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
            mRiggerTransaction.remove(fragmentTAG).commit();
        }
    }

    @Override
    public int getContainerViewId() {
        return mContainerViewId;
    }

    @Override
    public boolean isBondContainerView() {
        return mStickyStack;
    }

    @Override
    public boolean isLazyLoading() {
        return false;
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

    @Override
    public boolean isAbleSwipeBack() {
        return mSwiper != null && mSwiper.enable();
    }

    @Nullable
    @Override
    public SwipeLayout getSwipeLayout() {
        return buildSwipeLayout();
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
     * Add a fragment and set the fragment's mAnimations
     */
    private void addFragmentWithAnim(Fragment fragment, int containerViewId) {
        _FragmentRigger rigger = (_FragmentRigger) Rigger.getRigger(fragment);
        mRiggerTransaction.setCustomAnimations(rigger.mEnterAnim, rigger.mPopExitAnim);
        mRiggerTransaction.add(containerViewId, fragment, rigger.getFragmentTAG());
    }

    /**
     * Show a fragment and set the fragment's mAnimations
     */
    private void showFragmentWithAnim(Fragment fragment) {
        _FragmentRigger rigger = (_FragmentRigger) Rigger.getRigger(fragment);
        mRiggerTransaction.setCustomAnimations(rigger.mPopEnterAnim, rigger.mExitAnim);
        mRiggerTransaction.show(rigger.getFragmentTAG());
    }

    /**
     * Throw the exception.
     */
    void throwException(RiggerException e) {
        throw e;
    }

    /**
     * Return fragments tag which the fragment's view is visible and is add onto the container view.
     *
     * @param containerViewId The container view's id to be found.
     *
     * @return The fragment tags.
     */
    private String[] getVisibleFragmentTags(@IdRes int containerViewId) {
        List<String> result = new ArrayList<>();
        String[] fragmentTags = mStackManager.getFragmentTags(containerViewId);
        for (String tag : fragmentTags) {
            Fragment fragment = mRiggerTransaction.find(tag);
            if (fragment != null && !fragment.isHidden() &&
                    fragment.getView() != null && fragment.getView().getVisibility() == View.VISIBLE) {
                result.add(tag);
            }
        }
        return result.toArray(new String[result.size()]);
    }

    SwipeLayout buildSwipeLayout() {
        if (mSwiper == null) return null;
        if (mSwipeLayout != null) return mSwipeLayout;
        mSwipeLayout = new SwipeLayout(mContext);
        mSwipeLayout.setPuppetHost(getPuppetHost());
        // setup params
        mSwipeLayout.setEnableSwipe(mSwiper.enable());
        mSwipeLayout.setParallaxOffset(mSwiper.parallaxOffset());
        mSwipeLayout.setSwipeEdgeSide(mSwiper.edgeSide());
        mSwipeLayout.setStickyWithHost(mStickyStack);
        mSwipeLayout.setScrimColor(mSwiper.scrimColor());
        mSwipeLayout.setScrimMaxAlpha(mSwiper.scrimMaxAlpha());
        mSwipeLayout.setShadowDrawable(mSwiper.shadowDrawable());
        mSwipeLayout.setShadowWidth(mSwiper.shadowWidth());

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mSwipeLayout.setLayoutParams(params);
        mSwipeLayout.setBackgroundColor(Color.TRANSPARENT);
        return mSwipeLayout;
    }

    /**
     * Sets the to be animated view on hardware layer during the animation.Note
     * that calling this will replace any existing animation listener on the animation
     * with a new one, as animations do not support more than one listeners. Therefore,
     * animations that already have listeners should do the layer change operations
     * in their existing listeners, rather than calling this function.
     */
    void setHWLayerAnimListenerIfAlpha(View v, Animation anim) {
        if (v == null || anim == null) {
            return;
        }
        if (shouldRunOnHWLayer(v, anim)) {
            anim.setAnimationListener(new AnimateOnHWLayerIfNeededListener(v, anim));
        }
    }

    static boolean shouldRunOnHWLayer(View v, Animation anim) {
        return v.getLayerType() == View.LAYER_TYPE_NONE
                && ViewCompat.hasOverlappingRendering(v)
                && modifiesAlpha(anim);
    }

    private static boolean modifiesAlpha(Animation anim) {
        if (anim instanceof AlphaAnimation) {
            return true;
        } else if (anim instanceof AnimationSet) {
            List<Animation> anims = ((AnimationSet) anim).getAnimations();
            for (int i = 0; i < anims.size(); i++) {
                if (anims.get(i) instanceof AlphaAnimation) {
                    return true;
                }
            }
        }
        return false;
    }

    static class AnimateOnHWLayerIfNeededListener implements AnimationListener {

        private boolean mShouldRunOnHWLayer = false;
        private View mView;

        AnimateOnHWLayerIfNeededListener(final View v, Animation anim) {
            if (v == null || anim == null) {
                return;
            }
            mView = v;
        }

        @Override
        @CallSuper
        public void onAnimationStart(Animation animation) {
            mShouldRunOnHWLayer = shouldRunOnHWLayer(mView, animation);
            if (mShouldRunOnHWLayer) {
                mView.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    }
                });
            }
        }

        @Override
        @CallSuper
        public void onAnimationEnd(Animation animation) {
            if (mShouldRunOnHWLayer) {
                mView.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.setLayerType(View.LAYER_TYPE_NONE, null);
                    }
                });
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }
}
