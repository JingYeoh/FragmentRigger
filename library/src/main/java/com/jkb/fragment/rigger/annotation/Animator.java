package com.jkb.fragment.rigger.annotation;

import android.support.annotation.AnimRes;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to set specific animation resources to run for the fragments that are
 * entering and exiting in this transaction.
 * This annotation can only be effective for fragment.
 *
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Dec 05,2017
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Animator {

  /**
   * entering animation resource id.
   */
  @AnimRes
  int enter();

  /**
   * exiting animation resource id.
   */
  @AnimRes
  int exit();
}
