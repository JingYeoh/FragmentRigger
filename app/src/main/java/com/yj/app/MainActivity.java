package com.yj.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import com.jkb.fragment.rigger.annotation.Puppet;
import com.jkb.fragment.rigger.rigger.Rigger;
import com.yj.app.fragment.FirstFragment;

@Puppet(containerViewId = R.id.mainContent)
public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    findViewById(R.id.main_bt).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Rigger.getRigger(MainActivity.this).startFragment(FirstFragment.newInstance());
      }
    });
  }
}
