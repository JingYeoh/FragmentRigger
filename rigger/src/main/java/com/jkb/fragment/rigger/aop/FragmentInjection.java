package com.jkb.fragment.rigger.aop;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Using AspectJ tools reach AOP. this class is used to inject
 * {@link com.jkb.fragment.rigger.rigger.Rigger} to Fragment's lifecycle methods.
 *
 * @author JingYeoh
 * <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 * <a href="https://github.com/justkiddingbaby">Github</a>
 * <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 19,2017
 */
@Aspect
public class FragmentInjection extends RiggerInjection {

    //****************PointCut***********************************

    @Pointcut("execution(androidx.fragment.app.Fragment+.new()) && annotatedWithPuppet()")
    public void constructor() {
    }

    @Pointcut("execution(* androidx.fragment.app.Fragment+.onAttach(..)) && annotatedWithPuppet()")
    public void onAttach() {
    }

    @Pointcut("execution(* androidx.fragment.app.Fragment+.onCreate(..)) && annotatedWithPuppet()")
    public void onCreate() {
    }

    @Pointcut("call(* androidx.fragment.app.Fragment+.onViewCreated(..)) && annotatedWithPuppet()")
    public void onViewCreated() {
    }

    @Pointcut("execution(* androidx.fragment.app.Fragment+.onCreateView(..)) && annotatedWithPuppet()")
    public void onCreateView() {
    }

    @Pointcut("execution(* androidx.fragment.app.Fragment+.onResume(..)) && annotatedWithPuppet()")
    public void onResume() {
    }

    @Pointcut("execution(* androidx.fragment.app.Fragment+.onSaveInstanceState(..)) && annotatedWithPuppet()")
    public void onSaveInstanceState() {
    }

    @Pointcut("execution(* androidx.fragment.app.Fragment+.onDestroy(..)) && annotatedWithPuppet()")
    public void onDestroy() {
    }

    @Pointcut("execution(* androidx.fragment.app.Fragment+.onDetach(..)) && annotatedWithPuppet()")
    public void onDetach() {
    }

    @Pointcut("execution(* androidx.fragment.app.Fragment+.setUserVisibleHint(..)) && annotatedWithPuppet()")
    public void setUserVisibleHint() {
    }

    //****************Process***********************************

    @Around("constructor()")
    public Object constructProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Object puppet = joinPoint.getTarget();
        //Only inject the class that marked by Puppet annotation.

        Method onAttach = getRiggerMethod("onPuppetConstructor", Object.class);
        onAttach.invoke(getRiggerInstance(), puppet);
        return result;
    }

    @Around("onAttach()")
    public Object onAttachProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Object puppet = joinPoint.getTarget();
        //Only inject the class that marked by Puppet annotation.
        Object[] args = joinPoint.getArgs();

        Method onAttach = getRiggerMethod("onAttach", Object.class, Context.class);
        onAttach.invoke(getRiggerInstance(), puppet, args[0]);
        return result;
    }

    @Around("onCreate()")
    public Object onCreateProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Object puppet = joinPoint.getTarget();
        //Only inject the class that marked by Puppet annotation.
        Object[] args = joinPoint.getArgs();

        Method onCreate = getRiggerMethod("onCreate", Object.class, Bundle.class);
        onCreate.invoke(getRiggerInstance(), puppet, args[0]);
        return result;
    }

    @Around("onCreateView()")
    public Object onCreateViewProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Object puppet = joinPoint.getTarget();
        Object[] args = joinPoint.getArgs();

        Method onCreate = getRiggerMethod("onCreateView", Object.class, LayoutInflater.class, ViewGroup.class,
            Bundle.class, View.class);
        Object riggerResult = onCreate.invoke(getRiggerInstance(), puppet, args[0], args[1], args[2], result);
        return riggerResult == null ? result : riggerResult;
    }

    @After("onViewCreated()")
    public void onViewCreatedProcess(JoinPoint joinPoint) throws Throwable {
        Object puppet = joinPoint.getTarget();
        //Only inject the class that marked by Puppet annotation.
        Object[] args = joinPoint.getArgs();

        Method onCreate = getRiggerMethod("onViewCreated", Object.class, View.class, Bundle.class);
        onCreate.invoke(getRiggerInstance(), puppet, args[0], args[1]);
    }

    @Around("onResume()")
    public Object onResumeProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Object puppet = joinPoint.getTarget();
        //Only inject the class that marked by Puppet annotation.

        Method onPause = getRiggerMethod("onResume", Object.class);
        onPause.invoke(getRiggerInstance(), puppet);
        return result;
    }

    @Around("onSaveInstanceState()")
    public Object onSaveInstanceStateProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Object puppet = joinPoint.getTarget();
        //Only inject the class that marked by Puppet annotation.
        Object[] args = joinPoint.getArgs();

        Method onSaveInstanceState = getRiggerMethod("onSaveInstanceState", Object.class, Bundle.class);
        onSaveInstanceState.invoke(getRiggerInstance(), puppet, args[0]);
        return result;
    }

    @Around("onDestroy()")
    public Object onDestroyProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Object puppet = joinPoint.getTarget();
        //Only inject the class that marked by Puppet annotation.

        Method onDestroy = getRiggerMethod("onDestroy", Object.class);
        onDestroy.invoke(getRiggerInstance(), puppet);
        return result;
    }

    @Around("onDetach()")
    public Object onDetachProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Object puppet = joinPoint.getTarget();
        //Only inject the class that marked by Puppet annotation.

        Method onDestroy = getRiggerMethod("onDetach", Object.class);
        onDestroy.invoke(getRiggerInstance(), puppet);
        return result;
    }

    @Around("setUserVisibleHint()")
    public Object setUserVisibleHintProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Object puppet = joinPoint.getTarget();
        //Only inject the class that marked by Puppet annotation.
        Object[] args = joinPoint.getArgs();

        Method onDestroy = getRiggerMethod("setUserVisibleHint", Object.class, boolean.class);
        onDestroy.invoke(getRiggerInstance(), puppet, args[0]);
        return result;
    }
}
