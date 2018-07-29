package com.jkb.fragment.rigger.exception;

/**
 * Rigger is already exist.
 *
 * @author JingYeoh
 * <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 * <a href="https://github.com/justkiddingbaby">Github</a>
 * <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 21,2017
 */

public class NotExistException extends RiggerException {

    public NotExistException(String fragmentTag) {
        super(fragmentTag + "is not found in stack");
    }
}
