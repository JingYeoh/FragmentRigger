package com.yj.app;

import android.os.Bundle;

import com.jkb.fragment.rigger.rigger.Rigger;
import com.jkb.fragment.swiper.annotation.Swiper;
import com.yj.app.base.BaseActivity;

@Swiper(parallaxOffset = 0.5f)
public class MainActivity extends BaseActivity {

    @Override
    protected void init(Bundle savedInstanceState) {
        Rigger.enableDebugLogging(true);
        if (savedInstanceState == null) {
            Rigger.getRigger(this).startFragment(TestFragment.newInstance());
        }
    }
}
