package com.jkb.fragment.rigger.exception;

/**
 * UnSupported operation exception.
 *
 * @author JingYeoh
 * <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 * <a href="https://github.com/justkiddingbaby">Github</a>
 * <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 22,2017
 */

public class UnSupportException extends RiggerException {

    public UnSupportException(String operate) {
        super("Unsupported operation [" + operate + "],please check your code");
    }
}
