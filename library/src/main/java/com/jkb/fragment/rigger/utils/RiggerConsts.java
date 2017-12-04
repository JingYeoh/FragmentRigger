package com.jkb.fragment.rigger.utils;

/**
 * Used to save the static constant.
 *
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
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
   * OnBackPressed method's name.this method must be defined with the specified params.
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
   * Method for lazy load.this method must be defined with the specified params.
   * For example:
   * public void onLazyLoadViewCreated(Bundle savedInstanceState)
   */
  String METHOD_ON_LAZYLOAD_VIEW_CREATED = "onLazyLoadViewCreated";
  /**
   * getLazyLoadContainerViewId method's name.this method must be defined with the specified params.
   * For example:
   * public int getLazyLoadContainerViewId()
   */
  String METHOD_GET_LAZYLOAD_CONTAINERVIEWID = "getLazyLoadContainerViewId";
}
