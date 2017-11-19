package com.jkb.fragment.rigger.aop;

import com.jkb.fragment.rigger.utils.Logger;
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

  @Pointcut("execution(* android.app.Activity+.onCreate(..))")
  public void onCreatePointCut() {
  }

  @Pointcut("execution(* android.app.Activity+.onResumeFragments(..))")
  public void onResumeFragmentsPointCut() {
  }

  @Pointcut("execution(* android.app.Activity+.onPostResume(..))")
  public void onPostResumePointCut() {
  }

  @Pointcut("execution(* android.app.Activity+.onPause(..))")
  public void onPausePointCut() {
  }

  @Pointcut("execution(* android.app.Activity+.onSaveInstanceState(..))")
  public void onSaveInstanceStatePointCut() {
  }

  @Pointcut("execution(* android.app.Activity+.onDestroy(..))")
  public void onDestroyPointCut() {
  }

  @Pointcut("execution(* android.app.Activity+.onBackPressed(..))")
  public void onBackPressedPointCut() {
  }

  //****************Process***********************************

  @Around("onCreatePointCut()")
  public Object onCreateProcess(ProceedingJoinPoint joinPoint) throws Throwable {
    Object result = joinPoint.proceed();
    Logger.i(joinPoint.getTarget(), joinPoint.getSignature().getName());
    return result;
  }

  @Around("onResumeFragmentsPointCut()")
  public Object onResumeFragmentsProcess(ProceedingJoinPoint joinPoint) throws Throwable {
    Object result = joinPoint.proceed();
    Logger.i(joinPoint.getTarget(), joinPoint.getSignature().getName());
    return result;
  }

  @Around("onPausePointCut()")
  public Object onPauseProcess(ProceedingJoinPoint joinPoint) throws Throwable {
    Object result = joinPoint.proceed();
    Logger.i(joinPoint.getTarget(), joinPoint.getSignature().getName());
    return result;
  }

  @Around("onSaveInstanceStatePointCut()")
  public Object onSaveInstanceStateProcess(ProceedingJoinPoint joinPoint) throws Throwable {
    Object result = joinPoint.proceed();
    Logger.i(joinPoint.getTarget(), joinPoint.getSignature().getName());
    return result;
  }

  @Around("onDestroyPointCut()")
  public Object onDestroyProcess(ProceedingJoinPoint joinPoint) throws Throwable {
    Object result = joinPoint.proceed();
    Logger.i(joinPoint.getTarget(), joinPoint.getSignature().getName());
    return result;
  }

  @Around("onBackPressedPointCut()")
  public Object onBackPressedProcess(ProceedingJoinPoint joinPoint) throws Throwable {
    Object result = joinPoint.proceed();
    Logger.i(joinPoint.getTarget(), joinPoint.getSignature().getName());
    return result;
  }
}
