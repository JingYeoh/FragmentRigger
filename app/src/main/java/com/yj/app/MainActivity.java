package com.yj.app;

import android.os.Bundle;
import com.jkb.fragment.rigger.rigger.Rigger;
import com.yj.app.base.BaseActivity;
import com.yj.app.fragment.FirstFragment;

public class MainActivity extends BaseActivity {

  @Override
  protected void init(Bundle savedInstanceState) {
    if (savedInstanceState == null) {
      Rigger.getRigger(this).startFragment(FirstFragment.newInstance(1));
    }
  }
}
