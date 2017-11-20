package com.yj.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.jkb.fragment.rigger.annotation.Puppet;
import com.jkb.fragment.rigger.utils.Logger;

@Puppet
public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Logger.i(this, "onCreate");
  }
}
