package com.jkb.fragment.swiper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Support for Activity/Fragment exit page by swipe edge.
 * <p>
 * This can only support the fragment added into the rigger stack for Fragment.
 *
 * @author JingYeoh
 * <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 * <a href="https://github.com/justkiddingbaby">Github</a>
 * <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Feb 09,2018.
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Swiper {

    /**
     * Allow or not to exit Activity/Fragment by swipe edge.
     *
     * @return The default value is true.
     */
    boolean enable() default true;

    /**
     * The edge that can be swiped.
     * The default value is {@link SwipeEdge#LEFT}
     *
     * @return if the value contained {@link SwipeEdge#NONE}, then it is not allowed to swipe.
     */
    SwipeEdge[] edgeSide() default SwipeEdge.LEFT;

    /**
     * Allow or not to show the parallax effect.
     * <p>
     * The parallax effect will be showed as the value > 0. Otherwise , the parallax will not be showed.
     * <p>
     * The effect range is from 0.0f to 1.0f.
     *
     * @return Default value is 1.0f.
     */
    float parallaxOffset() default 1.0f;
}
