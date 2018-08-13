package com.jkb.fragment.rigger.aop;

import com.jkb.fragment.reflect.RiggerReflectManager;
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
        Class riggerClazz = RiggerReflectManager.getInstance().getClass(Rigger.class.getName());
        assert riggerClazz != null;
        Method getInstance = RiggerReflectManager.getInstance().getDeclaredMethod(riggerClazz, "getInstance");
        assert getInstance != null;
        return (Rigger) getInstance.invoke(null);
    }

    /**
     * Returns the method object of Rigger by reflect.
     */
    Method getRiggerMethod(String methodName, Class<?>... params) {
        Class clazz = RiggerReflectManager.getInstance().getClass(Rigger.class.getName());
        return RiggerReflectManager.getInstance().getDeclaredMethod(clazz, methodName, params);
    }
}
