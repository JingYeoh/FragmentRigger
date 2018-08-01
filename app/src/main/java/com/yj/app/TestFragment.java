package com.yj.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.jkb.fragment.rigger.rigger.Rigger;
import com.jkb.fragment.rigger.utils.Logger;
import com.yj.app.base.BaseFragment;
import com.yj.app.test.lazyload.LazyLoadFragment;
import com.yj.app.test.replace.ReplaceFragment;
import com.yj.app.test.show.ShowFragment;
import com.yj.app.test.start.ResultFragment;
import com.yj.app.test.start.StartFragment;

/**
 * @author JingYeoh
 * <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 * <a href="https://github.com/justkiddingbaby">Github</a>
 * <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 23,2017
 */
public class TestFragment extends BaseFragment implements OnClickListener {

    public static TestFragment newInstance() {
        Bundle args = new Bundle();
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.frg_test;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        findViewById(R.id.fs_startFragment).setOnClickListener(this);
        findViewById(R.id.fs_startFragmentDelay).setOnClickListener(this);
        findViewById(R.id.fs_showFragment).setOnClickListener(this);
        findViewById(R.id.fs_replaceFragment).setOnClickListener(this);
        findViewById(R.id.fs_startFragmentForResult).setOnClickListener(this);
        findViewById(R.id.fs_lazyload).setOnClickListener(this);
        findViewById(R.id.fs_activity).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fs_startFragment:
                Rigger.getRigger(this).startFragment(StartFragment.newInstance(0));
                break;
            case R.id.fs_startFragmentDelay:
                startDelayed();
                break;
            case R.id.fs_startFragmentForResult:
                Rigger.getRigger(this).startFragmentForResult(this, ResultFragment.newInstance(), 1000);
                break;
            case R.id.fs_showFragment:
                Rigger.getRigger(this).startFragment(ShowFragment.newInstance());
                break;
            case R.id.fs_replaceFragment:
                Rigger.getRigger(this).startFragment(ReplaceFragment.newInstance());
                break;
            case R.id.fs_lazyload:
                Rigger.getRigger(this).startFragment(LazyLoadFragment.newInstance());
                break;
            case R.id.fs_activity:
                startActivity(new Intent(mContext, MainActivity.class));
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Rigger.getRigger(TestFragment.this).startFragment(StartFragment.newInstance(0));
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) mHandler.removeMessages(111);
    }

    private void startDelayed() {
        mHandler.sendEmptyMessageDelayed(111, 1000);
    }

    public void onFragmentResult(int requestCode, int resultCode, Bundle args) {
        if (resultCode != Rigger.RESULT_OK) return;
        String string = args.getString(BUNDLE_KEY);
        Logger.i(this, string);
        Toast.makeText(mContext, "requestCode=" + requestCode + ":::" + "resultCode=" + resultCode + "\n"
            + "result=" + string, Toast.LENGTH_SHORT).show();
    }
}
