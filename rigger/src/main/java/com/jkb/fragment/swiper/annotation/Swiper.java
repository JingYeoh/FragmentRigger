package com.jkb.fragment.swiper.annotation;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;

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
    @FloatRange(from = -1f, to = 1f)
    float parallaxOffset() default 1.0f;

    /**
     * The scrim color as the view is swiping.
     *
     * The scrim will not showed as the color is transparent.
     *
     * @return default color is black.
     */
    @ColorRes
    int scrimColor() default android.R.color.black;

    /**
     * The scrim max alpha.
     *
     * The scrim will not showed as the value is 0.
     *
     * @return default is 128
     */
    @IntRange(from = 0, to = 255)
    int scrimMaxAlpha() default 128;

    /**
     * The shadow drawable res for content view.
     *
     * If you don't need shadow , return null or 0.
     *
     * @return the max size is four , the value is left/right/top/bottom edge.
     */
    @DrawableRes
    int[] shadowDrawable() default 0;

    /**
     * The shadow width.
     *
     * If you don't need shadow , return 0.
     *
     * @return default value is 20 dp
     */
    int shadowWidth() default 20;
}
