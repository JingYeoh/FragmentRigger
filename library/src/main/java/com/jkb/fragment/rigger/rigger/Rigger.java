package com.jkb.fragment.rigger.rigger;

import static com.jkb.fragment.rigger.utils.RiggerConsts.METHOD_ONRIGGERBACKPRESSED;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import com.jkb.fragment.rigger.annotation.Puppet;
import com.jkb.fragment.rigger.exception.RiggerException;
import com.jkb.fragment.rigger.utils.Logger;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Rigger class.use this class to rig the puppet class.
 *
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 18,2017
 */

public final class Rigger {

  /** Standard fragment result: operation canceled. */
  public static final int RESULT_CANCELED = 0;
  /** Standard fragment result: operation succeeded. */
  public static final int RESULT_OK = -1;

  private static final String TAG_HEADER = "<<Rigger>>";
  private static volatile Rigger sInstance = null;
  private Map<Integer, IRigger> mPuppetMap;

  /**
   * Prevents this class from being instantiated.
   */
  private Rigger() {
    mPuppetMap = new HashMap<>();
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
   * @param object puppet class.must be a child class of {@link Fragment} or {@link AppCompatActivity}
   */
  @NonNull
  public static IRigger getRigger(Object object) {
    //filter the unsupported class
    if (!(object instanceof AppCompatActivity) && !(object instanceof Fragment)) {
      throw new RiggerException(
          "Puppet Annotation class can only used on android.app.Activity or android.support.v4.app.Fragment");
    }
    //filter the unsupported class
    Class<?> clazz = object.getClass();
    Puppet puppet = clazz.getAnnotation(Puppet.class);
    if (puppet == null) {
      throw new RiggerException("Can not find Puppet annotation.please add Puppet annotation for the class" +
          object.getClass().getSimpleName());
    }
    //get the object's address code.
    int code = System.identityHashCode(object);
    IRigger rigger = getInstance().mPuppetMap.get(code);
    if (rigger == null) {
      throw new RiggerException("UnKnown error " + object + "is not a puppet object");
    }
    return rigger;
  }

  /**
   * Returns or puts the object to rigger list.
   */
  private _Rigger createRigger(Object object) {
    //get the object's address code.
    int code = System.identityHashCode(object);
    _Rigger rigger = (_Rigger) mPuppetMap.get(code);
    if (rigger == null) {
      rigger = _Rigger.create(object);
      mPuppetMap.put(code, rigger);
      Logger.i(this, "add puppet " + object + " to rigger list");
    }
    return rigger;
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
   * {@link AppCompatActivity#onCreate(Bundle)}/{@link Fragment#onAttach(Context)} ()} to rigger class.
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
  }

  /**
   * Inject the Activity's lifecycle method
   * {@link AppCompatActivity#onBackPressed()} to rigger class.
   */
  private void onRiggerBackPressed(Object object) {
    Logger.i(object, TAG_HEADER + "onBackPressed");
    //if the object has this method,then call the native method,or call the proxy's method
    Class<?> clazz = object.getClass();
    try {
      Method onBackPressed = clazz.getMethod(METHOD_ONRIGGERBACKPRESSED);
      onBackPressed.invoke(object);
    } catch (Exception e) {
      createRigger(object).onRiggerBackPressed();
    }
  }
}
