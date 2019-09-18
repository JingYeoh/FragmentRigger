package com.jkb.fragment.rigger.rigger;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.jkb.fragment.rigger.exception.UnSupportException;
import com.jkb.fragment.rigger.utils.Logger;
import com.jkb.fragment.swiper.SwipeLayout;

/**
 * Activity Rigger.rig the Activity puppet.
 *
 * @author JingYeoh
 * <a href="mailto:yangjing9611@foxmail.com">Email me</a>
 * <a href="https://github.com/justkiddingbaby">Github</a>
 * <a href="http://blog.justkiddingbaby.com">Blog</a>
 * @since Nov 20,2017
 */

final class _ActivityRigger extends _Rigger {

    private AppCompatActivity mActivity;
    //data
    private boolean mIsResumed = false;

    _ActivityRigger(@NonNull AppCompatActivity activity) {
        super(activity);
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = mActivity;
        setupSwiper();
        if (mRiggerTransaction == null) {
            mRiggerTransaction = new RiggerTransactionImpl(this, mActivity.getSupportFragmentManager());
        }
        mStackManager.bindFragmentManager(mActivity.getSupportFragmentManager());
    }

    @Override
    public void onResumeFragments() {
        mIsResumed = true;
        mRiggerTransaction.commit();
    }

    @Override
    public void onResume() {
        mIsResumed = true;
    }

    @Override
    public void onPause() {
        mIsResumed = false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mStackManager.saveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        mStackManager.onDestroy();
        if (mActivity.isFinishing()) {
            mStackManager.clear();
            if (mRiggerTransaction != null) {
                mRiggerTransaction.removeAll();
            }
        }
    }

    @Override
    public boolean isResumed() {
        return mIsResumed;
    }

    @Override
    public void close() {
        mActivity.finish();
    }

    @Override
    public void closeWithoutTransaction() {
        close();
        mActivity.overridePendingTransition(0, 0);
    }

    @Override
    public void setFragmentTag(@NonNull String tag) {
        Logger.w(mActivity.getClass(), "setFragmentTag(String tag) method can only be called by Fragment");
    }

    @Override
    public String getFragmentTAG() {
        Logger.w(mActivity.getClass(), "getFragmentTAG() method can only be called by Fragment");
        return null;
    }

    @Override
    public void setResult(int resultCode, Bundle bundle) {
        throwException(new UnSupportException("setResult() method can only be called by Fragment"));
    }

    private void setupSwiper() {
        SwipeLayout swipeLayout = buildSwipeLayout();
        if (swipeLayout == null) return;

        TypedArray a = mActivity.getTheme().obtainStyledAttributes(new int[]{
                android.R.attr.windowBackground
        });
        int background = a.getResourceId(0, 0);
        a.recycle();
        // replace content view
        ViewGroup decor = (ViewGroup) mActivity.getWindow().getDecorView();
        decor.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        if (SwipeLayout.class.isInstance(decorChild)) {
            return;
        }
        decorChild.setBackgroundResource(background);
        decor.removeView(decorChild);
        swipeLayout.addView(decorChild);
        decor.addView(swipeLayout);
    }
}
