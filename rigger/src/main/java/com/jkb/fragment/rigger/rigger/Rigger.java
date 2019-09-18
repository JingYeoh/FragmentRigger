package com.jkb.fragment.rigger.rigger;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jkb.fragment.rigger.annotation.Puppet;
import com.jkb.fragment.rigger.exception.RiggerException;
import com.jkb.fragment.rigger.utils.Logger;

/**
 * Rigger class.use this class to rig the puppet class.
 *
 * @author JingYeoh
 * <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 * <a href="https://github.com/justkiddingbaby">Github</a>
 * <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 18,2017
 */

public final class Rigger {

    /**
     * Standard fragment result: operation canceled.
     */
    public static final int RESULT_CANCELED = 0;
    /**
     * Standard fragment result: operation succeeded.
     */
    public static final int RESULT_OK = -1;

    private static final String TAG_HEADER = "----------Rigger------------->";
    private static volatile Rigger sInstance = null;
    private SparseArray<IRigger> mPuppetMap;

    /**
     * Prevents this class from being instantiated.
     */
    private Rigger() {
        mPuppetMap = new SparseArray<>();
    }

    /**
     * Returns the instance of Rigger.
     */
    private static Rigger getInstance() {
        if (sInstance == null) {
            synchronized (Rigger.class) {
                if (sInstance == null) {
                    sInstance = new Rigger();
                }
            }
        }
        return sInstance;
    }

    /**
     * Control whether the framework's internal fragment rigger debugging
     * logs are turned on.  If enabled, you will see output in logcat as
     * the framework performs fragment operations.
     *
     * @param enable default is false.
     */
    public static void enableDebugLogging(boolean enable) {
        Logger.DEBUG = enable;
    }

    /**
     * Returns the Rigger that can rig the puppet object.
     *
     * @param puppet puppet class.must be a child class of {@link Fragment} or {@link AppCompatActivity}
     */
    @NonNull
    public static IRigger getRigger(Object puppet) {
        //filter the unsupported class
        if (!(puppet instanceof AppCompatActivity) && !(puppet instanceof Fragment)) {
            throw new RiggerException(
                "Puppet Annotation class can only used on android.app.Activity or android.support.v4.app.Fragment");
        }
        //filter the unsupported class
        Class<?> clazz = puppet.getClass();
        Puppet puppetAnnotation = clazz.getAnnotation(Puppet.class);
        if (puppetAnnotation == null) {
            throw new RiggerException("Can not find Puppet annotation.please add Puppet annotation for the class " +
                puppet.getClass().getName());
        }
        //get the object's address code.
        int code = System.identityHashCode(puppet);
        IRigger rigger = getInstance().mPuppetMap.get(code);
        if (rigger == null) {
            throw new RiggerException(
                "UnKnown error " + puppet + " is not added into rigger. please check your config or contact author.");
        }
        return rigger;
    }

    /**
     * Returns the Rigger object or puts the puppet to puppet list.
     */
    private _Rigger createRigger(Object puppet) {
        //get the object's address code.
        int code = System.identityHashCode(puppet);
        IRigger rigger = mPuppetMap.get(code);
        if (rigger == null) {
            rigger = _Rigger.create(puppet);
            mPuppetMap.put(code, rigger);
            Logger.i(puppet, "add puppet " + puppet + " to rigger list");
            return (_Rigger) rigger;
        }
        return (_Rigger) rigger;
    }

    /**
     * Remove a puppet object from caches.
     *
     * @param puppet Puppet object,Activity/Fragment
     *
     * @return the result of this process.
     */
    private boolean removeRigger(Object puppet) {
        int code = System.identityHashCode(puppet);
        if (mPuppetMap.indexOfKey(code) < 0) {
            return false;
        }
        mPuppetMap.remove(code);
        Logger.i(puppet, "remove puppet " + puppet + " from rigger list");
        return true;
    }

    /**
     * Inject the Fragment's construct method,and add to rigger list.
     *
     * @param object Fragment/Activity
     */
    private void onPuppetConstructor(Object object) {
        createRigger(object);
    }

    /**
     * Inject the Fragment's lifecycle method {@link Fragment#onAttach(Context)} ()} to rigger class.
     *
     * @param object the fragment puppet.
     */
    private void onAttach(Object object, Context context) {
        Logger.i(object, TAG_HEADER + "onAttach");
        createRigger(object).onAttach(context);
    }

    /**
     * Inject the Activity/Fragment's lifecycle method
     * {@link AppCompatActivity#onCreate(Bundle)}/{@link Fragment#onCreate(Bundle)} to rigger class.
     *
     * @param object             the puppet class.
     * @param savedInstanceState If the activity/fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    private void onCreate(Object object, Bundle savedInstanceState) {
        Logger.i(object, TAG_HEADER + "onCreate");
        createRigger(object).onCreate(savedInstanceState);
    }

    /**
     * Inject the Activity/Fragment's lifecycle method
     * {@link Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)} to rigger class.
     *
     * @param object             the puppet class.
     * @param savedInstanceState If the activity/fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    private Object onCreateView(Object object, LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState, @Nullable View view) {
        Logger.i(object, TAG_HEADER + "onCreateView");
        return createRigger(object).onCreateView(inflater, container, savedInstanceState, view);
    }

    /**
     * Inject the Fragment's lifecycle method {@link Fragment#onViewCreated(View, Bundle)}
     */
    private void onViewCreated(Object object, View view, @Nullable Bundle savedInstanceState) {
        Logger.i(object, TAG_HEADER + "onViewCreated");
        createRigger(object).onViewCreated(view, savedInstanceState);
    }

    /**
     * Inject the Fragment's lifecycle method {@link Fragment#onResume()} to rigger class.
     */
    private void onResume(Object object) {
        Logger.i(object, TAG_HEADER + "onResume");
        createRigger(object).onResume();
    }

    /**
     * Inject the Activity's lifecycle method {@link AppCompatActivity#onPostResume()} to rigger class.
     */
    private void onPostResume(Object object) {
        Logger.i(object, TAG_HEADER + "onPostResume");
    }

    /**
     * Inject the Activity's lifecycle method {@link AppCompatActivity#onPostResume()} to rigger class.
     */
    private void onResumeFragments(Object object) {
        Logger.i(object, TAG_HEADER + "onResumeFragments");
        createRigger(object).onResumeFragments();
    }

    /**
     * Inject the AppCompatActivity's lifecycle method {@link AppCompatActivity#onPause()} to rigger class.
     */
    private void onPause(Object object) {
        Logger.i(object, TAG_HEADER + "onPause");
        createRigger(object).onPause();
    }

    /**
     * Inject the Fragment's lifecycle method {@link AppCompatActivity#onSaveInstanceState(Bundle)}
     * /{@link Fragment#onSaveInstanceState(Bundle)} to rigger class.
     *
     * @param object   the puppet class.
     * @param outState in which to place your saved state.
     */
    private void onSaveInstanceState(Object object, Bundle outState) {
        Logger.i(object, TAG_HEADER + "onSaveInstanceState");
        createRigger(object).onSaveInstanceState(outState);
    }

    /**
     * Inject the Activity/Fragment's lifecycle method
     * {@link AppCompatActivity#onDestroy()}/{@link Fragment#onDestroy()} to rigger class.
     */
    private void onDestroy(Object object) {
        Logger.i(object, TAG_HEADER + "onDestroy");
        createRigger(object).onDestroy();
        removeRigger(object);
    }

    /**
     * Inject the Fragment's lifecycle method
     * {@link Fragment#onDetach()} ()} to rigger class.
     */
    private void onDetach(Object object) {
        Logger.i(object, TAG_HEADER + "onDetach");
        createRigger(object).onDetach();
        removeRigger(object);
    }

    /**
     * Inject the Activity's lifecycle method
     * {@link AppCompatActivity#onBackPressed()} to rigger class.
     */
    private void onBackPressed(Object object) {
        Logger.i(object, TAG_HEADER + "onBackPressed");
        createRigger(object).dispatchBackPressed();
    }

    /**
     * Inject the method {@link Fragment#setUserVisibleHint(boolean)}.
     */
    private void setUserVisibleHint(Object object, boolean isVisibleToUser) {
        Logger.i(object, TAG_HEADER + "setUserVisibleHint");
        createRigger(object).setUserVisibleHint(isVisibleToUser);
    }
}
