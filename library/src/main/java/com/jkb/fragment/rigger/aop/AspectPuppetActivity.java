package com.jkb.fragment.rigger.aop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.jkb.fragment.rigger.annotation.Puppet;
import com.jkb.fragment.rigger.rigger.Rigger;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Using AspectJ tools reach AOP. this class is used to inject
 * {@link com.jkb.fragment.rigger.rigger.Rigger} to Activity's lifecycle and other methods.
 *
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 19,2017
 */
@Aspect
public class AspectPuppetActivity {

  //****************PointCut***********************************

  @Pointcut("execution(* android.support.v4.app.FragmentActivity+.onCreate(..)) ")
  public void onCreatePointCut() {
  }

  @Pointcut("execution(* android.support.v4.app.FragmentActivity+.onResumeFragments(..))")
  public void onResumeFragmentsPointCut() {
  }

  @Pointcut("execution(* android.support.v4.app.FragmentActivity+.onPause(..))")
  public void onPausePointCut() {
  }

  @Pointcut("execution(* android.support.v4.app.FragmentActivity+.onResume(..))")
  public void onResumePointCut() {
  }

  @Pointcut("execution(* android.support.v4.app.FragmentActivity+.onSaveInstanceState(..))")
  public void onSaveInstanceStatePointCut() {
  }

  @Pointcut("execution(* android.support.v4.app.FragmentActivity+.onDestroy(..))")
  public void onDestroyPointCut() {
  }

  @Pointcut("execution(* android.support.v4.app.FragmentActivity+.onBackPressed(..))")
  public void onBackPressedPointCut() {
  }

  //****************Process***********************************

  @Around("onCreatePointCut()")
  public Object onCreateProcess(ProceedingJoinPoint joinPoint) throws Throwable {
    Object result = joinPoint.proceed();
    Object puppet = joinPoint.getTarget();
    //Only inject the class that marked by Puppet annotation.
    if (!isMarkedByPuppet(puppet)) return result;
    Object[] args = joinPoint.getArgs();

    Method onCreate = getRiggerMethod("onCreate", Object.class, Bundle.class);
    onCreate.invoke(getRiggerInstance(), puppet, args[0]);
    return result;
  }

  @Around("onResumeFragmentsPointCut()")
  public Object onResumeFragmentsProcess(ProceedingJoinPoint joinPoint) throws Throwable {
    Object result = joinPoint.proceed();
    Object puppet = joinPoint.getTarget();
    //Only inject the class that marked by Puppet annotation.
    if (!isMarkedByPuppet(puppet)) return result;

    Method onResumeFragments = getRiggerMethod("onResumeFragments", Object.class);
    onResumeFragments.invoke(getRiggerInstance(), puppet);
    return result;
  }

  @Around("onPausePointCut()")
  public Object onPauseProcess(ProceedingJoinPoint joinPoint) throws Throwable {
    Object result = joinPoint.proceed();
    Object puppet = joinPoint.getTarget();
    //Only inject the class that marked by Puppet annotation.
    if (!isMarkedByPuppet(puppet)) return result;

    Method onPause = getRiggerMethod("onPause", Object.class);
    onPause.invoke(getRiggerInstance(), puppet);
    return result;
  }

  @Around("onResumePointCut()")
  public Object onResumeProcess(ProceedingJoinPoint joinPoint) throws Throwable {
    Object result = joinPoint.proceed();
    Object puppet = joinPoint.getTarget();
    //Only inject the class that marked by Puppet annotation.
    if (!isMarkedByPuppet(puppet)) return result;

    Method onPause = getRiggerMethod("onResume", Object.class);
    onPause.invoke(getRiggerInstance(), puppet);
    return result;
  }

  @Around("onSaveInstanceStatePointCut()")
  public Object onSaveInstanceStateProcess(ProceedingJoinPoint joinPoint) throws Throwable {
    Object result = joinPoint.proceed();
    Object puppet = joinPoint.getTarget();
    //Only inject the class that marked by Puppet annotation.
    if (!isMarkedByPuppet(puppet)) return result;
    Object[] args = joinPoint.getArgs();

    Method onSaveInstanceState = getRiggerMethod("onSaveInstanceState", Object.class, Bundle.class);
    onSaveInstanceState.invoke(getRiggerInstance(), puppet, args[0]);
    return result;
  }

  @Around("onDestroyPointCut()")
  public Object onDestroyProcess(ProceedingJoinPoint joinPoint) throws Throwable {
    Object result = joinPoint.proceed();
    Object puppet = joinPoint.getTarget();
    //Only inject the class that marked by Puppet annotation.
    if (!isMarkedByPuppet(puppet)) return result;

    Method onDestroy = getRiggerMethod("onDestroy", Object.class);
    onDestroy.invoke(getRiggerInstance(), puppet);
    return result;
  }

  @Around("onBackPressedPointCut()")
  public Object onBackPressedProcess(ProceedingJoinPoint joinPoint) throws Throwable {
    Object result = joinPoint.proceed();
    Object puppet = joinPoint.getTarget();
    //Only inject the class that marked by Puppet annotation.
    if (!isMarkedByPuppet(puppet)) return result;

    Method onBackPressed = getRiggerMethod("onBackPressed", Object.class);
    onBackPressed.invoke(getRiggerInstance(), puppet);
    return result;
  }

  //******************helper***********************

  /**
   * Returns the instance of Rigger class by reflect.
   */
  private Rigger getRiggerInstance() throws Exception {
    Class<?> riggerClazz = Class.forName(Rigger.class.getName());
    Method getInstance = riggerClazz.getDeclaredMethod("getInstance");
    getInstance.setAccessible(true);
    return (Rigger) getInstance.invoke(null);
  }

  /**
   * Returns the method object of Rigger by reflect.
   */
  private Method getRiggerMethod(String methodName, Class<?>... params) throws Exception {
    Rigger rigger = getRiggerInstance();
    Class<? extends Rigger> clazz = rigger.getClass();
    Method method = clazz.getDeclaredMethod(methodName, params);
    method.setAccessible(true);
    return method;
  }

  /**
   * Returns the value of if the class is marked by Puppet annotation.
   */
  private boolean isMarkedByPuppet(Object object) {
    if (!(object instanceof FragmentActivity) && !(object instanceof Fragment)) {
      throw new UnsupportedOperationException(
          "Puppet Annotation class can only used on android.app.Activity or android.support.v4.app.Fragment");
    }
    Class<?> clazz = object.getClass();
    Puppet puppet = clazz.getAnnotation(Puppet.class);
    return puppet != null;
  }
}
