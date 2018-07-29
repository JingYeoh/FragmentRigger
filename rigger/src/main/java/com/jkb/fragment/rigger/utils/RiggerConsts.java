package com.jkb.fragment.rigger.utils;

/**
 * Used to save the static constant.
 *
 * @author JingYeoh
 * <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 * <a href="https://github.com/justkiddingbaby">Github</a>
 * <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 23,2017
 */

public interface RiggerConsts {

    /**
     * getContainerViewId method's name.this method must be defined with the specified params.
     * For example:
     * public int getContainerViewId()
     */
    String METHOD_GET_CONTAINERVIEWID = "getContainerViewId";
    /**
     * onBackPressed method's name.this method must be defined with the specified params.
     * For example:
     * public void onRiggerBackPressed()
     */
    String METHOD_ON_RIGGER_BACKPRESSED = "onRiggerBackPressed";
    /**
     * onFragmentResult method's name,this method must be defined with the specified params.
     * For example:
     * public void onFragmentResult(int requestCode,int resultCode,Bundle args)
     */
    String METHOD_ON_FRAGMENT_RESULT = "onFragmentResult";
    /**
     * Method name for lazy load.this method must be defined with the specified params.
     * For example:
     * public void onLazyLoadViewCreated(Bundle savedInstanceState)
     */
    String METHOD_ON_LAZYLOAD_VIEW_CREATED = "onLazyLoadViewCreated";

    /**
     * Method name for fragment animations.this method must be defined with the specified params.
     * This method return value is a {@link android.view.animation.Animation} array.the array's length must equal four.
     * Return[0]:enterAnim
     * Return[1]:exitAnim
     * Return[2]:popEnterAnim
     * Return[3]:popExitAnim
     * For example:
     * public int[] getPuppetAnimations()
     */
    String METHOD_GET_PUPPET_ANIMATIONS = "getPuppetAnimations";

    /**
     * Method name for set custom fragment tag.this method must be defined with empty params.
     * For example:
     * public String getFragmentTag()
     */
    String METHOD_GET_FRAGMENT_TAG = "getFragmentTag";

    /**
     * onBackPressed method's name.this method must be defined with boolean returns.
     * For example:
     * public boolean onInterruptBackPressed()
     */
    String METHOD_ON_INTERRUPT_BACKPRESSED = "onInterruptBackPressed";
}
