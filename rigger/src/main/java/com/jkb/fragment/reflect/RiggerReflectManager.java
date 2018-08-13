package com.jkb.fragment.reflect;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * The manager to help save reflect methods/classes.
 *
 * @author JingYeoh.
 * @since 2018-08-13
 */
public class RiggerReflectManager {

    private Map<String, Class> mClassMap;
    private Map<Class, Map<String, Method>> mMethodMap;
    private Map<Class, Map<Class, Annotation>> mAnnotationMap;

    private RiggerReflectManager() {
    }

    private static class Holder {

        private static RiggerReflectManager sInstance = new RiggerReflectManager();
    }

    public static RiggerReflectManager getInstance() {
        return Holder.sInstance;
    }

    public Class getClass(@NonNull String className) {
        if (mClassMap == null) {
            mClassMap = new HashMap<>();
        }
        Class clazz = mClassMap.get(className);
        if (clazz == null) {
            try {
                clazz = Class.forName(className);
                mClassMap.put(className, clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return clazz;
    }

    @Nullable
    public Method getDeclaredMethod(@NonNull Class clazz, @NonNull String fieldName, Class<?>... params) {
        if (mMethodMap == null) {
            mMethodMap = new HashMap<>();
        }
        Map<String, Method> methodsMap = mMethodMap.get(clazz);
        if (methodsMap == null) {
            methodsMap = new HashMap<>();
            mMethodMap.put(clazz, methodsMap);
        }
        Method method = methodsMap.get(fieldName);
        if (method == null) {
            try {
                method = clazz.getDeclaredMethod(fieldName, params);
                method.setAccessible(true);
                methodsMap.put(fieldName, method);
            } catch (NoSuchMethodException ignore) {
                return null;
            }
        }
        return method;
    }

    @Nullable
    public Method getMethod(@NonNull Class clazz, @NonNull String fieldName, Class<?>... params) {
        if (mMethodMap == null) {
            mMethodMap = new HashMap<>();
        }
        Map<String, Method> methodsMap = mMethodMap.get(clazz);
        if (methodsMap == null) {
            methodsMap = new HashMap<>();
            mMethodMap.put(clazz, methodsMap);
        }
        Method method = methodsMap.get(fieldName);
        if (method == null) {
            try {
                method = clazz.getMethod(fieldName, params);
                methodsMap.put(fieldName, method);
            } catch (NoSuchMethodException ignore) {
                return null;
            }
        }
        return method;
    }

    @Nullable
    public Annotation getAnnotation(@NonNull Class clazz, @NonNull Class annotationClazz) {
        if (mAnnotationMap == null) {
            mAnnotationMap = new HashMap<>();
        }
        Map<Class, Annotation> annotationsMap = mAnnotationMap.get(clazz);
        if (annotationsMap == null) {
            annotationsMap = new HashMap<>();
            mAnnotationMap.put(clazz, annotationsMap);
        }
        Annotation annotation = annotationsMap.get(annotationClazz);
        if (annotation == null) {
            annotation = clazz.getAnnotation(annotationClazz);
            if (annotation != null) {
                annotationsMap.put(annotationClazz, annotation);
            }
        }
        return annotation;
    }
}
