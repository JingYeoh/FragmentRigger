package com.jkb.fragment.swiper.aop;

import android.app.Activity;
import android.support.annotation.NonNull;
import com.jkb.fragment.reflect.RiggerReflectManager;
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
 * <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 * <a href="https://github.com/justkiddingbaby">Github</a>
 * <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 19,2017
 */
@Aspect
public class SwipeInjection {

    //****************PointCut***********************************

    /**
     * PointCut method.find all classes that is marked by
     * {@link com.jkb.fragment.swiper.annotation.Swiper} Annotation.
     */
    @Pointcut("@target(com.jkb.fragment.swiper.annotation.Swiper)")
    public void annotatedWithSwiper() {
    }

    /**
     * PointCut method.find all classes that is marked by
     * {@link com.jkb.fragment.rigger.annotation.Puppet} Annotation.
     */
    @Pointcut("@target(com.jkb.fragment.rigger.annotation.Puppet)")
    public void annotatedWithPuppet() {
    }

    //****************Helper************************************  && annotatedWithPuppet() && annotatedWithSwiper()

    @Pointcut("execution(* android.support.v4.app.FragmentActivity+.onCreate(..))")
    public void onCreate() {
    }

    @Pointcut("execution(* android.support.v4.app.FragmentActivity+.onDestroy(..))")
    public void onDestroy() {
    }

    //****************Process***********************************

    @Around("onCreate()")
    public Object onCreateProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Object puppet = joinPoint.getTarget();
        //Only inject the class that marked by Puppet annotation.

        Method onCreate = invokeSwiperMethod("addToStack", Activity.class);
        onCreate.invoke(getSwiperInstance(), puppet);
        return result;
    }

    @Around("onDestroy()")
    public Object onDestroyProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Object puppet = joinPoint.getTarget();
        //Only inject the class that marked by Puppet annotation.

        Method onDestroy = invokeSwiperMethod("removeFromStack", Activity.class);
        onDestroy.invoke(getSwiperInstance(), puppet);
        return result;
    }

    /**
     * Returns the instance of SwiperActivityManager class by reflect.
     */
    @NonNull
    Object getSwiperInstance() throws Exception {
        Class clazz = RiggerReflectManager.getInstance().getClass("com.jkb.fragment.swiper.SwipeActivityManager");
        assert clazz != null;
        Method getInstance = RiggerReflectManager.getInstance()
                .getDeclaredMethod(clazz, "getInstance");
        assert getInstance != null;
        return getInstance.invoke(null);
    }

    /**
     * Returns the method object of SwiperActivityManager by reflect.
     */
    Method invokeSwiperMethod(String methodName, Class<?>... params) throws Exception {
        return RiggerReflectManager.getInstance().getDeclaredMethod(getSwiperInstance().getClass(), methodName, params);
    }
}
