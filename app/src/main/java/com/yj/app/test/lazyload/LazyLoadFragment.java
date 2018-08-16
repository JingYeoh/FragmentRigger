package com.yj.app.test.lazyload;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.Toast;
import com.yj.app.R;
import com.yj.app.base.BaseFragment;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author JingYeoh
 * <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 * <a href="https://github.com/justkiddingbaby">Github</a>
 * <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Dec 04,2017
 */

public class LazyLoadFragment extends BaseFragment {

    public static LazyLoadFragment newInstance() {
        Bundle args = new Bundle();
        LazyLoadFragment fragment = new LazyLoadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private int mSelectedPosition = 0;
    private LazyLoadAdapter lazyLoadAdapter;
    private static boolean isExit = false;

    @Override
    protected int getContentView() {
        return R.layout.frg_lazyload;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initView();
        if (savedInstanceState != null) {
            mSelectedPosition = savedInstanceState.getInt(BUNDLE_KEY);
        }
        lazyLoadAdapter = new LazyLoadAdapter(mContext, getChildFragmentManager());
        viewPager.setAdapter(lazyLoadAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab == null) continue;
            tab.setCustomView(lazyLoadAdapter.getTabView(i));
        }
        lazyLoadAdapter.selectedTab(mSelectedPosition);
        initListener();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.fl_vp);
        tabLayout = (TabLayout) findViewById(R.id.fl_tab);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
    }

    private void initListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mSelectedPosition = tabLayout.getSelectedTabPosition();
                lazyLoadAdapter.selectedTab(mSelectedPosition);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_KEY, mSelectedPosition);
    }

    public boolean onRiggerBackPressed() {
        if (viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(0);
            return true;
        }
        if (!isExit) {
            isExit = true;
            Toast.makeText(mContext, "press again to close lazy load page.", Toast.LENGTH_SHORT).show();
            Timer tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
            return true;
        } else {
            return false;
        }
    }
}
