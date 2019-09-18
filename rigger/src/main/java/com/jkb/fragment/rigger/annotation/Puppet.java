package com.jkb.fragment.rigger.annotation;

import android.app.Activity;

import androidx.annotation.IdRes;

import com.jkb.fragment.rigger.rigger.Rigger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation class that can only be used on {@link Activity} or {@link  androidx.fragment.app.Fragment}, a
 * Activity/Fragment class that marked by this annotation can be rigged by {@link Rigger}.
 *
 * @author JingYeoh
 * <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 * <a href="https://github.com/justkiddingbaby">Github</a>
 * <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 18,2017
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Puppet {

    /**
     * Optional identifier of the container this fragment is
     * to be placed in.  If 0, it will not be placed in a container.
     */
    @IdRes
    int containerViewId() default 0;

    /**
     * Allow or not exit the host {@link android.app.Activity}/{@link  androidx.fragment.app.Fragment} as the
     * stack size = 1 .
     * <p>
     * If the value is true , the host object(Activity/Fragment) will be exit as the stack size = 1.
     * Otherwise , the host object(Activity/Fragment) will show it's containerView as the stack size = 1
     *
     * @return The default value is false.
     */
    boolean stickyStack() default false;
}
