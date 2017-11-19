package com.jkb.fragment.rigger.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Using AspectJ tools reach AOP. this class is used to inject
 * {@link com.jkb.fragment.rigger.rigger.Rigger} to Fragment's lifecycle methods.
 *
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 19,2017
 */
@Aspect
public class AspectPuppetFragentLifecycle {

  @Pointcut("execution(* android.support.v4.app.Fragment+.onAttach(..))")
  public void onAttactPointCut() {
  }

  @Pointcut("execution(* android.support.v4.app.Fragment+.onCreate(..))")
  public void onCreatePointCut() {
  }

  @Pointcut("execution(* android.support.v4.app.Fragment+.onResume(..))")
  public void onResumePointCut() {
  }

  @Pointcut("execution(* android.support.v4.app.Fragment+.onSaveInstanceState(..))")
  public void onSaveInstanceStatePointCut() {
  }

  @Pointcut("execution(* android.support.v4.app.Fragment+.onDestroy(..))")
  public void onDestroyPointCut() {
  }
}
