package com.jkb.fragment.rigger.aop;

import com.jkb.fragment.rigger.rigger.Rigger;

import java.lang.reflect.Method;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Using AspectJ tools reach AOP. this class is used to define common method.
 *
 * @author JingYeoh
 * <a href="mailto:yj@viroyal.cn">Email me</a>
 * <a href="https://github.com/justkiddingbaby">Github</a>
 * <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Jan 10,2018.
 */
class RiggerInjection {

    /**
     * PointCut method.find all classes that is marked by
     * {@link com.jkb.fragment.rigger.annotation.Puppet} Annotation.
     */
    @Pointcut("@target(com.jkb.fragment.rigger.annotation.Puppet)")
    public void annotatedWithPuppet() {
    }

    //****************Helper************************************

    /**
     * Returns the instance of Rigger class by reflect.
     */
    Rigger getRiggerInstance() throws Exception {
        Class<?> riggerClazz = Class.forName(Rigger.class.getName());
        Method getInstance = riggerClazz.getDeclaredMethod("getInstance");
        getInstance.setAccessible(true);
        return (Rigger) getInstance.invoke(null);
    }

    /**
     * Returns the method object of Rigger by reflect.
     */
    Method getRiggerMethod(String methodName, Class<?>... params) throws Exception {
        Rigger rigger = getRiggerInstance();
        Class<? extends Rigger> clazz = rigger.getClass();
        Method method = clazz.getDeclaredMethod(methodName, params);
        method.setAccessible(true);
        return method;
    }
}
