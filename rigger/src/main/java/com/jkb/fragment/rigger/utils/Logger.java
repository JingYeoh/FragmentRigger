package com.jkb.fragment.rigger.utils;

import android.util.Log;

/**
 * Log Utils for printf log info.
 *
 * @author JingYeoh
 * <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 * <a href="https://github.com/justkiddingbaby">Github</a>
 * <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 16,2017
 */

public class Logger {

    public static boolean DEBUG = false;

    /**
     * Send an {@link Log#INFO} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }

    /**
     * Send an {@link Log#VERBOSE} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void v(String tag, String msg) {
        if (DEBUG) {
            Log.v(tag, msg);
        }
    }

    /**
     * Send an {@link Log#WARN} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void w(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, msg);
        }
    }

    /**
     * Send an {@link Log#ERROR} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }

    /**
     * Send an {@link Log#DEBUG} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    /**
     * Send an {@link Log#INFO} log message.
     *
     * @param clz Get the class's simple name and Used to identify the source of a log message.  It
     *            usually identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void i(Class<?> clz, String msg) {
        i(clz.getSimpleName(), msg);
    }

    /**
     * Send an {@link Log#DEBUG} log message.
     *
     * @param clz Get the class's simple name and Used to identify the source of a log message.  It
     *            usually identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void d(Class<?> clz, String msg) {
        d(clz.getSimpleName(), msg);
    }

    /**
     * Send an {@link Log#VERBOSE} log message.
     *
     * @param clz Get the class's simple name and Used to identify the source of a log message.  It
     *            usually identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void v(Class<?> clz, String msg) {
        v(clz.getSimpleName(), msg);
    }

    /**
     * Send an {@link Log#WARN} log message.
     *
     * @param clz Get the class's simple name and Used to identify the source of a log message.  It
     *            usually identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void w(Class<?> clz, String msg) {
        w(clz.getSimpleName(), msg);
    }

    /**
     * Send an {@link Log#ERROR} log message.
     *
     * @param clz Get the class's simple name and Used to identify the source of a log message.  It
     *            usually identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void e(Class<?> clz, String msg) {
        e(clz.getSimpleName(), msg);
    }

    /**
     * Send an {@link Log#DEBUG} log message.
     *
     * @param object Get the object's class name and Used to identify the source of a log message.
     *               It usually identifies the class or activity where the log call occurs.
     * @param msg    The message you would like logged.
     */
    public static void d(Object object, String msg) {
        d(object.getClass(), msg);
    }

    /**
     * Send an {@link Log#INFO} log message.
     *
     * @param object Get the object's class name and Used to identify the source of a log message.
     *               It usually identifies the class or activity where the log call occurs.
     * @param msg    The message you would like logged.
     */
    public static void i(Object object, String msg) {
        i(object.getClass(), msg);
    }

    /**
     * Send an {@link Log#VERBOSE} log message.
     *
     * @param object Get the object's class name and Used to identify the source of a log message.
     *               It usually identifies the class or activity where the log call occurs.
     * @param msg    The message you would like logged.
     */
    public static void v(Object object, String msg) {
        v(object.getClass(), msg);
    }

    /**
     * Send an {@link Log#WARN} log message.
     *
     * @param object Get the object's class name and Used to identify the source of a log message.
     *               It usually identifies the class or activity where the log call occurs.
     * @param msg    The message you would like logged.
     */
    public static void w(Object object, String msg) {
        w(object.getClass(), msg);
    }

    /**
     * Send an {@link Log#ERROR} log message.
     *
     * @param object Get the object's class name and Used to identify the source of a log message.
     *               It usually identifies the class or activity where the log call occurs.
     * @param msg    The message you would like logged.
     */
    public static void e(Object object, String msg) {
        e(object.getClass(), msg);
    }
}
