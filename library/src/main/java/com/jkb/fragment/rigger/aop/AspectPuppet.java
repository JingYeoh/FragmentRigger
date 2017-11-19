package com.jkb.fragment.rigger.aop;

import android.app.Activity;
import android.support.v4.app.Fragment;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Using AspectJ tools reach AOP. this class is used to check if the annotation class
 * {@link com.jkb.fragment.rigger.rigger.Rigger}'s target is supported.
 *
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 19,2017
 */
@Aspect
public class AspectPuppet {

  /**
   * PointCut method.find all classes that is marked by
   * {@link com.jkb.fragment.rigger.annotation.Puppet} Annotation.
   */
  @Pointcut("execution(@com.jkb.fragment.rigger.annotation.Puppet * *(..))")
  public void puppetPointCut() {
  }

  /**
   * handle {@link #puppetPointCut()} method,and filter unsupported operation of
   * {@link com.jkb.fragment.rigger.annotation.Puppet} Annotation.
   */
  @Around("puppetPointCut()")
  public void processPuppetPointCut(JoinPoint point) {
    Object target = point.getTarget();
    if (!(target instanceof Activity) && !(target instanceof Fragment)) {
      throw new UnsupportedOperationException(
          "Puppet Annotation class can only used on android.app.Activity or android.support"
              + ".v4.app.Fragment");
    }
  }
}
