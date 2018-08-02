package com.yj.app.test.start;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jkb.fragment.rigger.annotation.Animator;
import com.jkb.fragment.rigger.annotation.Puppet;
import com.jkb.fragment.rigger.rigger.Rigger;
import com.jkb.fragment.rigger.utils.Logger;
import com.jkb.fragment.swiper.SwipeLayout;
import com.jkb.fragment.swiper.annotation.SwipeEdge;
import com.jkb.fragment.swiper.annotation.Swiper;
import com.yj.app.R;
import com.yj.app.base.BaseFragment;

import java.util.UUID;

/**
 * Demo of startFragment method.
 *
 * @author JingYeoh
 * <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 * <a href="https://github.com/justkiddingbaby">Github</a>
 * <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 21,2017
 */
@Swiper(edgeSide = SwipeEdge.ALL)
@Animator(enter = R.anim.push_left_in_no_alpha, exit = R.anim.push_right_out_no_alpha,
        popEnter = R.anim.push_right_in_no_alpha, popExit = R.anim.push_left_out_no_alpha)
@Puppet(containerViewId = R.id.firstContent)
public class StartFragment extends BaseFragment implements OnClickListener {

    public static StartFragment newInstance(int count) {
        Bundle args = new Bundle();
        args.putInt(BUNDLE_KEY, count);
        StartFragment fragment = new StartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Rigger.getRigger(this).getSwipeLayout().setShadowDrawable(new int[]{
                R.drawable.swiper_shadow_left, R.drawable.swiper_shadow_right,
                R.drawable.swiper_shadow_top, R.drawable.swiper_shadow_bottom
        });
        Rigger.getRigger(this).getSwipeLayout().setOnSwipeChangedListener(new SwipeLayout.OnSwipeChangedListener() {
            @Override
            public void onEdgeTouched(SwipeLayout v, SwipeEdge edge, Object puppet) {
                Logger.d(StartFragment.this, "onEdgeTouched::SwipeEdge=" + edge);
            }

            @Override
            public void onSwipeChanged(SwipeLayout v, Object puppet, float percent) {
                Logger.d(StartFragment.this, "onSwipeChanged::percent=" + percent);
            }

            @Override
            public void onSwipeBacked(SwipeLayout v, Object puppet) {
                Logger.d(StartFragment.this, "onSwipeBacked");
            }
        });
    }

    //data
    private int mCount;

    @Override
    protected int getContentView() {
        return R.layout.frg_start;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle args = savedInstanceState == null ? getArguments() : savedInstanceState;
        mCount = args.getInt(BUNDLE_KEY);
        ((TextView) findViewById(R.id.ft_tv)).setText(String.valueOf(mCount));

        findViewById(R.id.fs_startFragment).setOnClickListener(this);
        findViewById(R.id.fs_print).setOnClickListener(this);
        findViewById(R.id.fs_close).setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_KEY, mCount);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fs_startFragment:
                StartFragment startFragment = StartFragment.newInstance(mCount + 1);
//        Rigger.getRigger(startFragment).setFragmentTag(UUID.randomUUID().toString());
                Rigger.getRigger(this).startFragment(startFragment);
                break;
            case R.id.fs_print:
                Rigger.getRigger(this).printStack();
                break;
            case R.id.fs_close:
                Rigger.getRigger(this).close();
                break;
        }
    }

    public String getFragmentTag() {
        return UUID.randomUUID().toString();
    }
}
