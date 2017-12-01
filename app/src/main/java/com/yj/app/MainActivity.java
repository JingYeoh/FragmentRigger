package com.yj.app;

import android.os.Bundle;
import com.jkb.fragment.rigger.rigger.Rigger;
import com.jkb.fragment.rigger.utils.Logger;
import com.yj.app.base.BaseActivity;

public class MainActivity extends BaseActivity {

  @Override
  protected void init(Bundle savedInstanceState) {
    Rigger.enableDebugLogging(true);
    if (savedInstanceState == null) {
      Rigger.getRigger(this).startFragment(TestFragment.newInstance());
    }
    Logger.d(this, "Activity=" + hashCode());
  }
}
