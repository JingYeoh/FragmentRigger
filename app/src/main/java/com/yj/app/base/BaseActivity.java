package com.yj.app.base;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jkb.fragment.rigger.annotation.Puppet;
import com.yj.app.R;

/**
 * Base Activity.
 *
 * @author JingYeoh
 *         <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 *         <a href="https://github.com/justkiddingbaby">Github</a>
 *         <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 23,2017
 */
@Puppet(containerViewId = R.id.atyContent, stickyStack = true)
public abstract class BaseActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getContentView());
    init(savedInstanceState);
  }

  @LayoutRes
  protected int getContentView() {
    return R.layout.activity_content;
  }

  protected abstract void init(Bundle savedInstanceState);
}
