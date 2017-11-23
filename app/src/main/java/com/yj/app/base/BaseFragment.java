package com.yj.app.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jkb.fragment.rigger.annotation.Puppet;

/**
 * Base fragment.
 *
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 22,2017
 */
@Puppet
public abstract class BaseFragment extends Fragment {

  protected static final String BUNDLE_KEY = "/bundle/key";
  protected View mContentView;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mContentView = inflater.inflate(getContentView(), container, false);
    init(savedInstanceState);
    return mContentView;
  }

  @LayoutRes
  protected abstract int getContentView();

  protected abstract void init(Bundle savedInstanceState);

  protected View findViewById(@IdRes int id) {
    return mContentView.findViewById(id);
  }
}
